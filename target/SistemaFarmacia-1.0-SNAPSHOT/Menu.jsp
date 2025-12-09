<%-- 
    Document   : Menu
    Created on : 11 jun 2025, 9:53:31 a. m.
    Author     : Gaby Laínez
--%>

<%@page import="com.ues.edu.entities.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <%
        Usuario usuario = (Usuario) session.getAttribute("usuario"); 

        if (usuario == null || !usuario.isEstado()) {
            response.sendRedirect("Login.jsp"); 
            return;
        }

        String rolNombre = (usuario.getRol() != null) ? usuario.getRol().getNombreRol() : "";
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menú Principal</title>
        
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="css/diseñoMenu.css">
        
        <script defer src="js/Meny.js"></script>
    </head>
    <body>

        <button id="menu-toggle" class="btn shadow-sm">
            <i class="bi bi-list"></i>
        </button>

        <aside class="sidebar shadow" id="sidebar">
            <div class="sidebar-header">
                <h3><i class="bi bi-shop"></i> Farmacia</h3>
                <p class="small text-white-50">Bienvenido, <%= usuario.getUsuario() %></p>
                <hr class="text-white">
            </div>
            
            <ul class="nav flex-column">
                <%
                    if ("Administrador".equalsIgnoreCase(rolNombre)) {
                %>
                <li class="nav-item">
                    <a href="RegistroCategoria.jsp" class="nav-link">
                        <i class="bi bi-tags-fill me-2"></i> Registrar Categoría
                    </a>
                </li>       
                <li class="nav-item">
                    <a href="Medicina.jsp" class="nav-link">
                        <i class="bi bi-capsule me-2"></i> Registrar Medicamentos
                    </a>
                </li>
                <li class="nav-item">
                    <a href="RegistroUsuario.jsp" class="nav-link">
                        <i class="bi bi-people-fill me-2"></i> Registro Usuarios
                    </a>
                </li>
                <li class="nav-item">
                    <a href="RegistroPersona.jsp" class="nav-link">
                        <i class="bi bi-person-badge-fill me-2"></i> Registro Personal
                    </a>
                </li>

                <%
                    } else if ("Vendedor".equalsIgnoreCase(rolNombre)) {
                %>
                <li class="nav-item">
                    <a href="RegistrarVenta.jsp" class="nav-link">
                        <i class="bi bi-cart-check-fill me-2"></i> Registro Venta
                    </a>
                </li>
                <li class="nav-item">
                    <a href="ConsultaExistentes.jsp" class="nav-link">
                        <i class="bi bi-clipboard-data-fill me-2"></i> Existencias
                    </a>
                </li>
                
                <%
                    } else {
                %>
                <li class="nav-item">
                    <p class="text-warning p-3">Rol no reconocido</p>
                </li>
                <%
                        }
                %>
                
                <li class="nav-item mt-4">
                    <a href="Login.jsp" class="nav-link text-danger logout-link">
                        <i class="bi bi-box-arrow-left me-2"></i> Cerrar sesión
                    </a>
                </li>
            </ul>
        </aside>

        <main class="content">
            <div class="container-fluid">
                <div class="card bg-white bg-opacity-75 shadow-sm p-4 rounded">
                    <h1 class="display-5 text-dark">Bienvenid@s</h1>
                    <p class="lead">Seleccione una opción del menú para comenzar.</p>
                </div>
            </div>
        </main>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>