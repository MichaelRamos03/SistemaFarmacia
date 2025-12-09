<%-- 
    Document   : RegistroCategoria
    Created on : 27 may 2025, 8:43:46 p. m.
    Author     : Gaby Laínez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registro de Categorias</title>


        <!-- Inicio para que funcione class='dropdown m-b-10' -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js" integrity="sha384-w1Q4orYjBQndcko6MimVbzY0tgp4pWB4lZ7lr30WKz0vr/aWKhXdBNmNb5D92v7s" crossorigin="anonymous"></script>
        <!-- Fin para que funcione class='dropdown m-b-10' -->

        <!-- Inicio para que funcione sweetalert2@11 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <!-- Fin para que funcione class='Mensajes sweetalert2@11 -->

        <script  src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>



        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>


        <!-- jQuery -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <!-- Select2 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/css/select2.min.css" rel="stylesheet" />

        <!-- Select2 JS -->
        <script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/select2.min.js"></script>



        <!-- Parsley.js -->
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/parsley.min.js"></script>

        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/parsley.min.js"></script>


        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/i18n/es.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/i18n/es.extra.js"></script>


        <!-- DataTables JS -->
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
        <!-- DataTables CSS -->
        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">


    </head>
    <body class="fixed-left">

        <!-- Begin page -->
        <div id="wrapper">

            <!-- Start right Content here -->
            <div class="content-page">
                <!-- Start content -->
                <div class="content">
                    <!-- Top Bar Start -->

                    <!-- Top Bar End -->
                    <!-- ==================
                    PAGE CONTENT START
                    ================== -->
                    <style>
                        .error{
                            color: #EA553D;
                            font-size: 12px;
                            transition: .3s color ease-in-out;
                        }
                        .hidden{
                            color: #000000;
                            display: none;
                        }
                    </style>
                    <div class="page-content-wrapper">
                        <div class="container-fluid">
                            <div class="row">

                                <div class="row align-items-center mb-3">
                                    <div class="col-md-6 col-xl-6">
                                        <div class="mini-stat clearfix bg-white">
                                            <span class="mini-stat-icon bg-blue-grey mr-0 float-right">
                                                <i class="mdi mdi-black-mesa"></i>
                                            </span>
                                            <div class="mini-stat-info">
                                                <span id="categoria_registradas" class="counter text-blue-grey">0</span>
                                                Categorías registradas
                                            </div>
                                            <div class="clearfix"></div>
                                        </div>
                                    </div>

                                    <div class="col-md-3 col-xl-3">
                                        <div class="mini-stat clearfix bg-white" id="registrar_Categoria" style="cursor: pointer;">
                                            <span class="mini-stat-icon bg-teal mr-0 float-right"><i class="mdi mdi-account"></i></span>
                                            <div class="mini-stat-info">
                                                <span class="counter text-teal">Registrar</span>
                                                Categoría
                                            </div>
                                            <div class="clearfix"></div>
                                        </div>
                                    </div>

                                    <div class="col-md-3 col-xl-3 d-flex justify-content-end">
                                        <div class="mini-stat clearfix bg-white" id="mostrar_inactivas" style="cursor: pointer;">
                                            <span class="mini-stat-icon bg-danger mr-0 float-right"><i class="mdi mdi-restore"></i></span>
                                            <div class="mini-stat-info">
                                                <span class="counter text-danger">Reactivar</span>
                                                Categorías
                                            </div>
                                            <div class="clearfix"></div>
                                        </div>
                                    </div>
                                </div>


                                <div class="row mt-3" id="contenedor_inactivas" style="display:none;">
                                    <div class="col-12">
                                        <div class="card border border-danger">
                                            <div class="card-header bg-danger text-white">
                                                Categorías Inactivas
                                            </div>
                                            <div class="card-body" id="lista_categorias_inactivas">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <div class="card m-b-20">
                                        <div class="card-body">
                                            <div id="tablita"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div><!-- container -->
                    </div> <!-- Page content Wrapper -->
                </div> <!-- content -->

            </div>
            <!-- End Right content here -->




            <div class="modal fade" id="md_registrar_categoria" role="dialog"  aria-hidden="true">





                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">REGISTRAR NUEVA CATEGORIA<br>
                                <sub> Todos los campos son obligatorios</sub>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form name="formulario_Categoria" id="formulario_Categoria" data-parsley-validate>
                                <input type="hidden" id="opcion" name="opcion"
                                       value="insertar">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label id="labelId">Numero de Categoria</label>
                                            <input type="text" autocomplete="off" name="idCategoria" id="idCategoria"
                                                   val="" class="form-control" maxlength="5" />
                                        </div>
                                    </div>

                                    <div class="row">                                   
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>NOMBRE DE CATEGORIA</label>
                                                <input type="text" class="form-control"
                                                       name="nombreCategoria" id="nombreCategoria" required  data-parsley-required-message="El nombre de la categoría es obligatorio"
                                                       data-parsley-minlength="3"
                                                       data-parsley-minlength-message="Debe tener al menos 3 caracteres"
                                                       data-parsley-pattern="^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$"
                                                       data-parsley-pattern-message="Solo se permiten letras y espacios"/>
                                                <p class="error hidden" id="error">Campo requerido</p>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>DESCRIPCION</label>
                                                <input type="text" class="form-control" 
                                                       name="descripcion" id="descripcion" required data-parsley-required-message="La descripción es obligatoria"
                                                       data-parsley-minlength="5"
                                                       data-parsley-maxlength="100"
                                                       data-parsley-minlength-message="Debe tener al menos 5 caracteres"
                                                       data-parsley-maxlength-message="No puede exceder los 100 caracteres"/>
                                                <p class="error hidden" id="error">Campo requerido</p>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>ESTADO</label><br>

                                                <input type="radio" name="estado" id="estado_activo" value="activo" required/>
                                                <label for="estado_activo">Activo</label>

                                                <input type="radio" name="estado" id="estado_inactivo" value="inactivo" />
                                                <label for="estado_inactivo">Inactivo</label>

                                                <p class="error hidden" id="error">Campo requerido</p>
                                            </div>
                                        </div>
                                    </div>


                                    <div class="modal-footer">
                                        <button type="button" 
                                                class="btn btn-secondary cerrar" 
                                                data-dismiss="modal" aria-label="Close">Cerrar</button>
                                        <button type="submit" class="btn btn-primary">Guardar</button>
                                    </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>




            <script src="js/Categoria.js"></script>
