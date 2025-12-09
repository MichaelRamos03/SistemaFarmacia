<%-- 
    Document   : Login
    Created on : 4 jun 2025, 12:43:19 p. m.
    Author     : Gaby Laínez
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Iniciar Sesión | Farmacia</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">

        <link rel="stylesheet" href="css/estilosLogin.css">

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-5 col-lg-4">

                    <div class="card card-login">
                        <div class="login-header">
                            <h3 class="mb-0"><i class="bi bi-shop-window"></i> Farmacia</h3>
                            <small>Sistema de Gestión</small>
                        </div>

                        <div class="card-body p-4">
                            <h4 class="text-center mb-4 text-secondary">Bienvenido</h4>

                            <form id="loginForm">

                                <div class="mb-3">
                                    <label for="nombreUsuario" class="form-label text-muted">Usuario</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="bi bi-person-fill"></i></span>
                                        <input type="text" class="form-control" id="nombreUsuario" name="nombreUsuario" placeholder="Ingrese su usuario" required>
                                    </div>
                                </div>

                                <div class="mb-4">
                                    <label for="contrasenia" class="form-label text-muted">Contraseña</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="bi bi-lock-fill"></i></span>
                                        <input type="password" class="form-control" id="contrasenia" name="contrasenia" placeholder="Ingrese su contraseña" required>
                                    </div>
                                </div>

                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary btn-custom shadow-sm" id="btnIngresar">
                                        <span id="spinnerCarga" class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>

                                        <span id="textoBtn">INGRESAR</span> 

                                        <i class="bi bi-box-arrow-in-right" id="iconoBtn"></i>
                                    </button>
                                </div>

                                <div id="mensaje" class="mt-3 text-center text-danger fw-bold small"></div>
                        </div>

                        <div class="card-footer text-center py-3 bg-light border-0 rounded-bottom">
                            <small class="text-muted">¿Problemas para ingresar?</small>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script src="js/Login.js"></script> 
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>