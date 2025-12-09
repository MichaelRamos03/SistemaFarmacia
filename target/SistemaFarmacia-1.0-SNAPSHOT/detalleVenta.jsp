<%-- 
    Document   : detalleVenta
    Created on : 9 jun 2025
    Author     : Gaby Laínez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registro Detalle de Venta</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/css/select2.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="https://cdn.materialdesignicons.com/5.4.55/css/materialdesignicons.min.css">

        <style>
            .error { color: #EA553D; font-size: 12px; }
            .hidden { display: none; }
            .card-btn { cursor: pointer; transition: transform 0.2s; }
            .card-btn:hover { transform: scale(1.02); }
        </style>
    </head>
    <body class="bg-light">

        <div class="container-fluid mt-4">
            
            <div class="row mb-3">
                <div class="col-12">
                    <h3>Gestionando Venta #<span id="lblIdVenta" class="text-primary">...</span></h3>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="card shadow-sm p-3 mb-3">
                        <div class="d-flex align-items-center">
                            <div class="bg-light p-3 rounded-circle me-3">
                                <i class="mdi mdi-medical-bag text-primary" style="font-size: 24px;"></i>
                            </div>
                            <div>
                                <h5 class="mb-0 text-muted">Productos Agregados</h5>
                                <h3 class="mb-0" id="detalle_registradas">0</h3>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6" id="registrar_Detalle">
                    <div class="card shadow-sm p-3 mb-3 card-btn border-start border-5 border-success">
                        <div class="d-flex align-items-center">
                            <div class="bg-success bg-opacity-10 p-3 rounded-circle me-3">
                                <i class="mdi mdi-plus-circle text-success" style="font-size: 24px;"></i>
                            </div>
                            <div>
                                <h5 class="mb-0 text-muted">Acción</h5>
                                <h3 class="mb-0 text-success">Agregar Medicamento +</h3>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h5 class="card-title">Detalle de la Factura</h5>
                            <div id="tablita" class="table-responsive mt-3"></div>

                            <div class="mt-4 text-end border-top pt-3">
                                <button type="button" id="btnFinalizarCompra" class="btn btn-success btn-lg">
                                    <i class="mdi mdi-check-circle-outline"></i> Finalizar Compra
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="md_registrar_Detalle" role="dialog" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title">Agregar Producto</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form name="formulario_Detalle" id="formulario_Detalle" data-parsley-validate>
                            <input type="hidden" id="opcion" name="opcion" value="insertar" />
                            <input type="hidden" id="idVenta" name="idVenta" value="">
                            <input type="hidden" id="idDetalle_venta" name="idDetalle_venta" value="">

                            <div class="row mb-3">
                                <div class="col-md-12">
                                    <label class="form-label">Medicamento</label>
                                    <select id="idMedicamento" name="idMedicamento" class="form-control" style="width: 100%;" required>
                                        <option value="" disabled selected>Seleccione una opción</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Cantidad:</label>
                                    <input type="number" class="form-control" name="cantidadProducto" id="cantidadProducto" required min="1" />
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                                <button type="submit" class="btn btn-primary">Agregar al Carrito</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/select2.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/parsley.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/i18n/es.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>

        <script src="js/DetalleVenta.js"></script>

    </body>
</html>