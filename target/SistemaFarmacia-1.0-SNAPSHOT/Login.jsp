<%-- 
    Document   : Login
    Created on : 4 jun 2025, 12:43:19 p. m.
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

        <link rel="stylesheet" href="css/estilosFarmacia.css">

        <style>
            body {
                background-color: #f3f4f7;
                background-image: linear-gradient(135deg, #f3f4f7 0%, #e8eaf6 100%);
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .card-login {
                border: none;
                border-radius: 15px;
                box-shadow: 0 10px 25px rgba(90, 44, 160, 0.15);
                overflow: hidden; /* Para que el header no se salga del borde */
            }
            .login-header {
                background-color: var(--color-primary, #5a2ca0); /* Usa tu variable o el color fijo */
                color: white;
                padding: 2rem 1rem;
                text-align: center;
                border-bottom: 5px solid var(--color-accent, #7a42c4);
            }
            .input-group-text {
                background-color: #f8f9fa;
                border-color: #ced4da;
                color: var(--color-primary, #5a2ca0);
            }
            .form-control:focus {
                border-color: var(--color-primary, #5a2ca0);
                box-shadow: 0 0 0 0.25rem rgba(90, 44, 160, 0.25);
            }
            .btn-login {
                background-color: var(--color-primary, #5a2ca0);
                border: none;
                padding: 12px;
                font-weight: bold;
                letter-spacing: 1px;
                transition: all 0.3s;
            }
            .btn-login:hover {
                background-color: var(--color-accent, #7a42c4);
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(90, 44, 160, 0.3);
            }
        </style>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-5 col-lg-4">

                    <div class="card card-login">
                        
                        <div class="login-header">
                            <div class="mb-2">
                                <i class="bi bi-shop-window display-4"></i>
                            </div>
                            <h3 class="mb-0 fw-bold">Farmacia</h3>
                            <small class="text-white-50">Sistema de Gestión</small>
                        </div>

                        <div class="card-body p-4 pt-5">
                            <h5 class="text-center mb-4 text-dark-purple fw-bold">Bienvenido</h5>

                            <form id="loginForm">

                                <div class="mb-3">
                                    <label for="nombreUsuario" class="form-label text-muted small fw-bold">USUARIO</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
                                        <input type="text" class="form-control" id="nombreUsuario" name="nombreUsuario" placeholder="Ingrese su usuario" required>
                                    </div>
                                </div>

                                <div class="mb-4">
                                    <label for="contrasenia" class="form-label text-muted small fw-bold">CONTRASEÑA</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                                        <input type="password" class="form-control" id="contrasenia" name="contrasenia" placeholder="Ingrese su contraseña" required>
                                    </div>
                                </div>

                                <div class="d-grid gap-2 mt-4">
                                    <button type="submit" class="btn btn-primary btn-login text-white shadow-sm" id="btnIngresar">
                                        <span id="spinnerCarga" class="spinner-border spinner-border-sm d-none me-2" role="status" aria-hidden="true"></span>
                                        <span id="textoBtn">INGRESAR</span> 
                                        <i class="bi bi-box-arrow-in-right ms-2" id="iconoBtn"></i>
                                    </button>
                                </div>

                            </form>
                        </div>

                        <div class="card-footer text-center py-3 bg-light border-0">
                            <small class="text-muted">¿Olvidó su contraseña? <a href="#" class="text-decoration-none text-purple fw-bold">Recuperar</a></small>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script src="js/Login.js"></script> 
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>