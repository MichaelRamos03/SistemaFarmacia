<%-- 
    Document   : detalleVenta
    Created on : 9 jun 2025
    Author     : Gaby Laínez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Detalle de Venta | Farmacia</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        
        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
        <link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.4.1/css/responsive.bootstrap5.min.css">

        <link href="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/css/select2.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css" />
        
        <link rel="stylesheet" href="css/estilosFarmacia.css">

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body class="bg-light">

        <div class="container-fluid p-4">
            
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold text-dark-purple mb-0"><i class="bi bi-basket2-fill"></i> Gestionar Venta</h2>
                    <small class="text-muted">Editando Transacción #<span id="lblIdVenta" class="fw-bold text-purple">...</span></small>
                </div>
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb mb-0">
                        <li class="breadcrumb-item"><a href="RegistrarVenta.jsp" class="text-decoration-none text-muted">Ventas</a></li>
                        <li class="breadcrumb-item active text-purple" aria-current="page">Detalles</li>
                    </ol>
                </nav>
            </div>

            <div class="row g-3 mb-4">
                <div class="col-md-6">
                    <div class="card card-stat shadow-sm border-0 h-100">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted text-uppercase mb-1">Productos Agregados</h6>
                                <h2 class="fw-bold text-dark-purple mb-0" id="detalle_registradas">0</h2>
                            </div>
                            <div class="icon-box bg-primary-soft text-purple rounded-circle">
                                <i class="bi bi-box-seam fs-4"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card card-action shadow-sm border-0 h-100 bg-purple text-white" id="registrar_Detalle">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h5 class="fw-bold mb-0">Agregar Producto</h5>
                                <small class="text-white-50">Click para añadir al carrito</small>
                            </div>
                            <div class="icon-box bg-white text-purple rounded-circle">
                                <i class="bi bi-plus-lg fs-4"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="card shadow border-0 rounded-3">
                <div class="card-header bg-white py-3 border-bottom d-flex justify-content-between align-items-center">
                    <h5 class="mb-0 text-secondary"><i class="bi bi-receipt"></i> Detalle de la Factura</h5>
                </div>
                <div class="card-body">
                    <div id="tablita" class="table-responsive">
                        <div class="text-center py-5">
                            <div class="spinner-border text-primary" role="status"></div>
                            <p class="mt-2 text-muted">Cargando carrito...</p>
                        </div>
                    </div>

                    <div class="d-flex justify-content-end mt-4 pt-3 border-top">
                        <button type="button" id="btnFinalizarCompra" class="btn btn-success px-4 py-2 shadow-sm rounded-pill">
                            <i class="bi bi-check-circle-fill me-2"></i> Finalizar Compra
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="md_registrar_Detalle" tabindex="-1" aria-hidden="true" data-bs-backdrop="static">
            <div class="modal-dialog modal-dialog-centered modal-lg">
                <div class="modal-content border-0 shadow-lg">
                    
                    <div class="modal-header bg-purple text-white">
                        <h5 class="modal-title">
                            <i class="bi bi-capsule"></i> Agregar Medicamento
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    
                    <div class="modal-body p-4">
                        <form name="formulario_Detalle" id="formulario_Detalle" data-parsley-validate>
                            <input type="hidden" id="opcion" name="opcion" value="insertar" />
                            <input type="hidden" id="idVenta" name="idVenta" value="">
                            <input type="hidden" id="idDetalle_venta" name="idDetalle_venta" value="">

                            <div class="alert alert-info py-2 small mb-4">
                                <i class="bi bi-info-circle-fill"></i> Seleccione el medicamento y la cantidad a despachar.
                            </div>

                            <div class="row g-3">
                                <div class="col-md-12">
                                    <label class="form-label fw-bold text-muted">Medicamento</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="bi bi-search text-purple"></i></span>
                                        <div class="flex-grow-1">
                                            <select id="idMedicamento" name="idMedicamento" class="form-select" style="width: 100%;" required>
                                                <option value="" disabled selected>Buscar producto...</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-bold text-muted">Cantidad</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="bi bi-123 text-purple"></i></span>
                                        <input type="number" class="form-control" name="cantidadProducto" id="cantidadProducto" required min="1" placeholder="Ej: 5" />
                                    </div>
                                </div>
                            </div>

                            <div class="modal-footer px-0 pb-0 border-top-0 mt-4">
                                <button type="button" class="btn btn-light text-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-primary bg-purple border-0 px-4 shadow-sm">
                                    <i class="bi bi-cart-plus"></i> Agregar al Carrito
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
        <script src="https://cdn.datatables.net/responsive/2.4.1/js/dataTables.responsive.min.js"></script>
        <script src="https://cdn.datatables.net/responsive/2.4.1/js/responsive.bootstrap5.min.js"></script>
        
        <script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/select2.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/parsley.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/i18n/es.js"></script>

        <script src="js/DetalleVenta.js"></script>

    </body>
</html>