<%-- 
    Document   : Medicina
    Created on : 6 jun 2025, 1:43:43 p. m.
    Author     : Gaby Laínez
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Inventario de Medicamentos</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">

        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/dataTables.bootstrap5.min.css">
        <link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.4.1/css/responsive.bootstrap5.min.css">

        <link href="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/css/select2.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css" />

        <link rel="stylesheet" href="css/estilosUsuario.css">
    </head>
    <body class="bg-light">

        <div class="container-fluid p-4">

            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="fw-bold text-dark-purple"><i class="bi bi-capsule"></i> Inventario de Farmacia</h2>
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="Menu.jsp">Inicio</a></li>
                        <li class="breadcrumb-item active" aria-current="page">Medicamentos</li>
                    </ol>
                </nav>
            </div>

            <div class="row g-4 mb-4">
                <div class="col-md-4">
                    <div class="card card-stat shadow-sm border-0 h-100">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted text-uppercase mb-1">Total en Inventario</h6>
                                <h2 class="fw-bold text-primary mb-0" id="medicina_registradas">0</h2>
                            </div>
                            <div class="icon-box bg-primary-soft text-primary rounded-circle">
                                <i class="bi bi-box-seam fs-4"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card card-action shadow-sm border-0 h-100 bg-purple text-white" id="registrar_medicina" style="cursor: pointer;">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h5 class="fw-bold mb-0">Nuevo Producto</h5>
                                <small class="text-white-50">Click para agregar</small>
                            </div>
                            <div class="icon-box bg-white text-purple rounded-circle">
                                <i class="bi bi-plus-lg fs-4 fw-bold"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card card-action shadow-sm border-0 h-100 bg-pink text-white" id="mostrar_inactivas" style="cursor: pointer;">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h5 class="fw-bold mb-0">Papelera</h5>
                                <small class="text-white-50">Ver eliminados</small>
                            </div>
                            <div class="icon-box bg-white text-pink rounded-circle">
                                <i class="bi bi-trash fs-4"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mb-4" id="contenedor_inactivas" style="display:none;">
                <div class="col-12">
                    <div class="card border-0 shadow-sm border-start border-danger border-5">
                        <div class="card-header bg-white py-3">
                            <h5 class="card-title text-danger mb-0"><i class="bi bi-exclamation-triangle-fill"></i> Productos Inactivos</h5>
                        </div>
                        <div class="card-body bg-light">
                            <div id="lista_medicamentos_inactivos">
                                <p class="text-muted text-center mb-0">Cargando...</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="card shadow border-0 rounded-3">
                <div class="card-header bg-white py-3 border-bottom">
                    <h5 class="mb-0 text-secondary fw-bold">Listado de Medicamentos</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive" id="tablita">
                        <div class="text-center py-5">
                            <div class="spinner-border text-primary" role="status">
                                <span class="visually-hidden">Cargando...</span>
                            </div>
                            <p class="mt-2 text-muted">Cargando inventario...</p>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="modal fade" id="md_registrar_medicina" tabindex="-1" aria-hidden="true" data-bs-backdrop="static">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-purple text-white">
                        <h5 class="modal-title" id="exampleModalLabel">
                            <i class="bi bi-capsule-pill"></i> Datos del Medicamento
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body p-4">
                        <form name="formulario_medicina" id="formulario_medicina" data-parsley-validate novalidate>
                            <input type="hidden" id="opcion" name="opcion" value="insertar">

                            <div class="alert alert-info py-2 small mb-3">
                                <i class="bi bi-info-circle-fill"></i> Todos los campos son obligatorios.
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6" id="divIdMedicamento" style="display:none;">
                                    <label class="form-label fw-bold text-muted">ID Medicamento</label>
                                    <input type="text" name="idMedicamento" id="idMedicamento" class="form-control bg-light" readonly>
                                </div>

                                <div class="col-md-12"> 
                                    <label class="form-label fw-bold text-muted">Categoría</label>
                                    <select id="idCategoria" name="idCategoria" class="form-select" required data-parsley-error-message="Seleccione una categoría">
                                        <option value="" disabled selected>Seleccione...</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold text-muted">Nombre Comercial</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="bi bi-tag"></i></span>
                                    <input type="text" class="form-control" name="nombre" id="nombre" required 
                                           placeholder="Ej: Acetaminofén 500mg"
                                           data-parsley-pattern="^[a-zA-ZÁÉÍÓÚáéíóúñÑ0-9\s]+$"
                                           data-parsley-error-message="Solo letras, números y espacios">
                                </div>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold text-muted">Cantidad (Stock)</label>
                                    <input type="text" class="form-control" name="cantidadExistencias" id="cantidadExistencias" required 
                                           data-parsley-type="integer" data-parsley-min="1">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold text-muted">Precio Unitario ($)</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light">$</span>
                                        <input type="text" class="form-control" name="precioUnidad" id="precioUnidad" required 
                                               data-parsley-type="number" step="0.01">
                                    </div>
                                </div>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold text-muted">Fecha de Ingreso</label>
                                    <input type="date" class="form-control" name="fechaIngreso" id="fechaIngreso" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold text-muted">Fecha de Expiración</label>
                                    <input type="date" class="form-control" name="fechaDeExpiracion" id="fechaDeExpiracion" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold text-muted">Descripción</label>
                                <input type="text" class="form-control" name="descripcion" id="descripcion" required 
                                       data-parsley-minlength="5">
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold text-muted d-block">Estado</label>
                                <div class="btn-group w-100" role="group">
                                    <input type="radio" class="btn-check" name="activo" id="estado_activo" value="activo" checked required>
                                    <label class="btn btn-outline-success" for="estado_activo"><i class="bi bi-check-circle"></i> Activo</label>

                                    <input type="radio" class="btn-check" name="activo" id="estado_inactivo" value="inactivo">
                                    <label class="btn btn-outline-danger" for="estado_inactivo"><i class="bi bi-x-circle"></i> Inactivo</label>
                                </div>
                            </div>

                            <div class="modal-footer px-0 pb-0 border-top-0 mt-4">
                                <button type="button" class="btn btn-light text-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-primary bg-purple border-0 px-4 shadow-sm">
                                    <i class="bi bi-save"></i> Guardar Producto
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/select2.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/parsley.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/i18n/es.js"></script>
        <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.4/js/dataTables.bootstrap5.min.js"></script>

        <script src="js/Medicina.js"></script>
    </body>
</html>