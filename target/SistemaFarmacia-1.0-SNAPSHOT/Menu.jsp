<%-- 
    Document   : Menu
    Created on : 11 jun 2025
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
        String nombreUsuario = (usuario.getUsuario() != null) ? usuario.getUsuario() : "Usuario";
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Menú Principal | Farmacia</title>
        
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        
        <link rel="stylesheet" href="css/diseñoMenu.css">
        
        <script defer src="js/Meny.js"></script>
    </head>
    <body>

        <button id="menu-toggle" class="shadow">
            <i class="bi bi-list"></i>
        </button>

        <aside class="sidebar shadow-lg" id="sidebar">
            <div class="sidebar-header">
                <h3><i class="bi bi-capsule-pill"></i> Farmacia</h3>
                <small class="text-white-50 d-block">Sistema de Gestión</small>
                
                <div class="mt-3 d-flex align-items-center justify-content-center p-2 rounded" style="background: rgba(255,255,255,0.1);">
                    <i class="bi bi-person-circle fs-4 me-2"></i>
                    <span class="text-truncate" style="max-width: 150px;"><%= nombreUsuario %></span>
                </div>
            </div>
            
            <ul class="nav flex-column mt-2">
                
                <% if ("Administrador".equalsIgnoreCase(rolNombre)) { %>
                
                <li class="nav-item">
                    <a href="RegistroCategoria.jsp" class="nav-link">
                        <i class="bi bi-tags-fill"></i> Categorías
                    </a>
                </li>       
                <li class="nav-item">
                    <a href="Medicina.jsp" class="nav-link">
                        <i class="bi bi-capsule"></i> Medicamentos
                    </a>
                </li>
                <li class="nav-item">
                    <a href="RegistroUsuario.jsp" class="nav-link">
                        <i class="bi bi-people-fill"></i> Usuarios
                    </a>
                </li>
                <li class="nav-item">
                    <a href="RegistroPersona.jsp" class="nav-link">
                        <i class="bi bi-person-badge-fill"></i> Personal
                    </a>
                </li>

                <% } else if ("Vendedor".equalsIgnoreCase(rolNombre)) { %>
                
                <li class="nav-item">
                    <a href="RegistrarVenta.jsp" class="nav-link">
                        <i class="bi bi-cart-check-fill"></i> Registrar Venta
                    </a>
                </li>
                <li class="nav-item">
                    <a href="ConsultaExistentes.jsp" class="nav-link">
                        <i class="bi bi-clipboard-data-fill"></i> Consultar Stock
                    </a>
                </li>
                
                <% } else { %>
                <li class="nav-item">
                    <p class="text-warning p-3 m-0"><i class="bi bi-exclamation-triangle"></i> Rol sin accesos definidos</p>
                </li>
                <% } %>
                
                <li class="nav-item mt-auto">
                    <a href="Login.jsp" class="nav-link logout-link">
                        <i class="bi bi-box-arrow-left"></i> Cerrar Sesión
                    </a>
                </li>
            </ul>
        </aside>

        <main class="content">
            <div class="container-fluid">
                
                <div class="card card-welcome shadow-sm p-5">
                    <i class="bi bi-shop bg-decoration"></i>
                    
                    <div class="position-relative" style="z-index: 1;">
                        <h1 class="display-5 fw-bold text-dark-purple mb-3">
                            ¡Hola, <%= nombreUsuario %>!
                        </h1>
                        <p class="lead text-secondary">
                            Bienvenido al sistema. Usa el menú lateral (<i class="bi bi-list"></i>) para acceder a las opciones disponibles para tu rol de <strong><%= rolNombre %></strong>.
                        </p>
                        <hr class="my-4">
                        <a href="#" id="btn-explorar" class="btn btn-primary bg-purple border-0 px-4 py-2 shadow-sm">
                            <i class="bi bi-arrow-right-circle me-2"></i> Comenzar
                        </a>
                    </div>
                </div>

            </div>
        </main>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"></script>
        
        <script>
            document.getElementById('btn-explorar').addEventListener('click', function(e) {
                e.preventDefault();
                // Simulamos click en el toggle
                document.getElementById('menu-toggle').click();
            });
        </script>
    </body>
</html>