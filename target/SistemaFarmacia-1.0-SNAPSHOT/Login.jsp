<%-- 
    Document   : Login
    Created on : 4 jun 2025, 12:43:19 p. m.
    Author     : Gaby Laínez
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h2 class="mt-5">Login</h2>
        <form id="loginForm">
            <div class="mb-3">
                <label for="nombreUsuario" class="form-label">Usuario</label>
                <input type="text" class="form-control" id="nombreUsuario" name="nombreUsuario" required>
            </div>
            <div class="mb-3">
                <label for="contrasenia" class="form-label">Contraseña</label>
                <input type="password" class="form-control" id="contrasenia" name="contrasenia" required>
            </div>
            <button type="submit" class="btn btn-primary">Iniciar sesión</button>
        </form>
        <div id="mensaje" class="mt-3 text-danger"></div>
    </div>

    <script src="js/Login.js"></script> 
</body>
</html>
