<%-- 
    Document   : usuario
    Created on : 1 jun 2025, 9:15:53 p. m.
    Author     : Gaby Laínez
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestión de Usuarios</title>

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
                <h2 class="fw-bold text-dark-purple"><i class="bi bi-people-fill"></i> Control de Usuarios</h2>
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="Menu.jsp">Inicio</a></li>
                        <li class="breadcrumb-item active" aria-current="page">Usuarios</li>
                    </ol>
                </nav>
            </div>

            <div class="row g-3 mb-4">

                <div class="col-md-4">
                    <div class="card card-stat shadow-sm border-0 h-100">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted text-uppercase mb-1">Usuarios Activos</h6>
                                <h2 class="fw-bold text-primary mb-0" id="usuarios_registrados">0</h2>
                            </div>
                            <div class="icon-box bg-primary-soft text-primary rounded-circle">
                                <i class="bi bi-person-check-fill fs-4"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card card-action shadow-sm border-0 h-100 bg-purple text-white" id="registrar_usuario" style="cursor: pointer;">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h5 class="fw-bold mb-0">Nuevo Usuario</h5>
                                <small class="text-white-50">Click para registrar</small>
                            </div>
                            <div class="icon-box bg-white text-purple rounded-circle">
                                <i class="bi bi-person-plus-fill fs-4"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card card-action shadow-sm border-0 h-100 bg-pink text-white" id="mostrar_inactivas" style="cursor: pointer;">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h5 class="fw-bold mb-0">Papelera</h5>
                                <small class="text-white-50">Ver inactivos</small>
                            </div>
                            <div class="icon-box bg-white text-pink rounded-circle">
                                <i class="bi bi-arrow-counterclockwise fs-4"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mb-4" id="contenedor_inactivas" style="display:none;">
                <div class="col-12">
                    <div class="card border-0 shadow-sm border-start border-danger border-5">
                        <div class="card-header bg-white py-3">
                            <h5 class="card-title text-danger mb-0"><i class="bi bi-exclamation-triangle"></i> Usuarios Inactivos</h5>
                        </div>
                        <div class="card-body bg-light" id="lista_usuarios_inactivas">
                        </div>
                    </div>
                </div>
            </div>

            <div class="card shadow border-0 rounded-3">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0 text-secondary">Listado General</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive" id="tablita">
                        <div class="text-center py-5">
                            <div class="spinner-border text-primary" role="status">
                                <span class="visually-hidden">Cargando...</span>
                            </div>
                            <p class="mt-2 text-muted">Cargando datos...</p>
                        </div>
                    </div>
                </div>
            </div>

        </div> <div class="modal fade" id="md_registrar_usuario" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" data-bs-backdrop="static">
            <div class="modal-dialog modal-dialog-centered modal-lg">
                <div class="modal-content border-0 shadow-lg">

                    <div class="modal-header bg-purple text-white">
                        <h5 class="modal-title" id="exampleModalLabel">
                            <i class="bi bi-person-badge"></i> Registro de Usuario
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body p-4">
                        <form name="formulario_usuario" id="formulario_usuario" data-parsley-validate novalidate>
                            <input type="hidden" id="opcion" name="opcion" value="insertar">

                            <div class="alert alert-info py-2 small mb-3">
                                <i class="bi bi-info-circle"></i> Todos los campos marcados son obligatorios.
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6" id="divIdUsuario" style="display: none;"> <label id="labelIdUsuario" class="form-label fw-bold text-muted">ID Usuario</label>
                                    <input type="text" name="idUsuario" id="idUsuario" class="form-control bg-light" readonly>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-bold text-muted">Rol de Usuario</label>
                                    <select id="nombreRol" name="nombreRol" class="form-select" required data-parsley-error-message="Seleccione un rol">
                                        <option value="" disabled selected>Seleccione...</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold text-muted">Nombre de Usuario</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="bi bi-person"></i></span>
                                        <input type="text" class="form-control" name="Usuario" id="Usuario" required 
                                               data-parsley-error-message="El nombre es obligatorio">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold text-muted">Contraseña</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="bi bi-key"></i></span>
                                        <input type="password" class="form-control" name="contrasenia" id="contrasenia" required 
                                               data-parsley-minlength="6"
                                               data-parsley-error-message="Mínimo 6 caracteres">
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold text-muted d-block">Estado de la cuenta</label>
                                <div class="btn-group w-100" role="group">
                                    <input type="radio" class="btn-check" name="estado" id="estado_activo" value="activo" checked required>
                                    <label class="btn btn-outline-success" for="estado_activo"><i class="bi bi-check-circle"></i> Activo</label>

                                    <input type="radio" class="btn-check" name="estado" id="estado_inactivo" value="inactivo">
                                    <label class="btn btn-outline-danger" for="estado_inactivo"><i class="bi bi-x-circle"></i> Inactivo</label>
                                </div>
                            </div>

                            <div class="modal-footer px-0 pb-0 border-top-0 mt-4">
                                <button type="button" class="btn btn-light text-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-primary bg-purple border-0 px-4 shadow-sm">
                                    <i class="bi bi-save"></i> Guardar Usuario
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

        <script src="js/Usuario.js"></script>

    </body>
</html>