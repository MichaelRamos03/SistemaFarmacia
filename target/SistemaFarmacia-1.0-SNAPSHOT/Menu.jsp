<%-- 
    Document   : Menu
    Created on : 11 jun 2025, 9:53:31 a. m.
    Author     : Gaby Laínez
--%>

<%@page import="com.ues.edu.entities.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
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
        <title>Menu</title>
        <link rel="stylesheet" href="css/diseñoMenu.css">
        <script defer src="js/Meny.js"></script>
    </head>
    <body>
        <button id="menu-toggle">☰</button>

        <aside class="sidebar" id="sidebar">
            <h2>Menú</h2>
            <ul>
                <%
                    if ("Administrador".equalsIgnoreCase(rolNombre)) {
                %>
                <li><a href="RegistroCategoria.jsp">Registrar Categoría</a></li>        
                <li><a href="Medicina.jsp">Registrar Medicamentos</a></li>
                 <li><a href="RegistroUsuario.jsp">Registro Usuarios</a></li>
                <li><a href="RegistroPersona.jsp">Registro Personal</a></li>

                    <%
                    } else if ("Vendedor".equalsIgnoreCase(rolNombre)) {
                    %>
                <li><a href="RegistrarVenta.jsp">Registro Venta</a></li>
                <li><a href="ConsultaExistentes.jsp">Existencias de medicamentos</a></li>
                
                    <%
                    } else {
                    %>
                <li><p>Rol no reconocido</p></li>
                    <%
                        }
                    %>
                <li><a href="Login.jsp">Cerrar sesión</a></li>
            </ul>
        </aside>

        <main class="content">
            <h1>Bienvenid@s</h1>
        </main>
    </body>
</html>
