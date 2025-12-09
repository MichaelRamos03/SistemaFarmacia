<%-- 
    Document   : RegistrarVenta
    Created on : 9 jun 2025
    Author     : Gaby Laínez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registro Venta</title>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js"></script>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        
        <link href="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/css/select2.min.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/select2.min.js"></script>
        
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/parsley.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/i18n/es.js"></script>
        
        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>

        <style>
            .error { color: #EA553D; font-size: 12px; transition: .3s color ease-in-out; }
            .hidden { display: none; }
            /* Ajuste para que los botones de la tabla no se corten */
            .dropdown-menu { z-index: 10000 !important; }
            .table-responsive { overflow: visible !important; }
        </style>
    </head>
    <body class="fixed-left">

        <div id="wrapper">
            <div class="content-page">
                <div class="content">
                    <div class="page-content-wrapper">
                        <div class="container-fluid">
                            
                            <div class="row mt-4">
                                <div class="col-md-6">
                                    <div class="card p-3 mb-3 shadow-sm">
                                        <div class="d-flex align-items-center">
                                            <div class="p-2 bg-light rounded-circle mr-3">
                                                <i class="mdi mdi-black-mesa" style="font-size: 24px;"></i>
                                            </div>
                                            <div>
                                                <h5 class="mb-0 text-muted">Ventas Registradas</h5>
                                                <h3 class="mb-0" id="venta_registradas">0</h3>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6" id="registrar_Venta" style="cursor: pointer;">
                                    <div class="card p-3 mb-3 shadow-sm border-left-primary" style="border-left: 5px solid #009688;">
                                        <div class="d-flex align-items-center">
                                            <div class="p-2 bg-light rounded-circle mr-3">
                                                <i class="mdi mdi-account-plus" style="font-size: 24px; color: #009688;"></i>
                                            </div>
                                            <div>
                                                <h5 class="mb-0 text-muted">Acción</h5>
                                                <h3 class="mb-0" style="color: #009688;">Nueva Venta +</h3>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-12">
                                    <div class="card m-b-20">
                                        <div class="card-body">
                                            <h4 class="mt-0 header-title">Listado de Ventas</h4>
                                            <div id="tablita" class="table-responsive"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div> 
                </div> 
            </div>

            <div class="modal fade" id="md_registrar_Venta" role="dialog" aria-hidden="true">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Registro nueva Venta<br><small class="text-muted">Todos los campos son obligatorios</small></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form name="formulario_Venta" id="formulario_Venta" data-parsley-validate>
                                <input type="hidden" id="opcion" name="opcion" value="insertar">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label id="labelVenta">Id Venta</label>
                                            <input type="text" autocomplete="off" name="idVenta" id="idVenta" class="form-control" maxlength="5" readonly />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Vendedor</label>
                                            <select id="id" name="id" data-parsley-required="true" class="form-control" style="width: 100%;">
                                                <option value="" disabled selected>Seleccione una opción</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Fecha Venta: </label>
                                        <input type="date" class="form-control" data-parsley-excluded="true" name="fechaVenta" id="fechaVenta" required/>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary cerrar" data-dismiss="modal">Cerrar</button>
                                    <button type="submit" class="btn btn-primary">Guardar e Ir a Detalles</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <script src="js/Venta.js"></script>
            
    </body>
</html>