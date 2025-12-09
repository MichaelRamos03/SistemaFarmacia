<%-- 
    Document   : ConsultaExistentes
    Created on : 21 may 2025, 4:31:37 p. m.
    Author     : Gaby Laínez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Consulta de medicamentos existentes</title>

        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- jQuery y DataTables -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>

        <!-- Estilo personalizado simple -->
        <style>
            body {
                background-color: #f4f9fc;
                font-family: 'Segoe UI', sans-serif;
            }
            .titulo {
                color: #333;
                font-weight: 600;
                border-left: 5px solid #0d6efd;
                padding-left: 10px;
            }
            .resumen {
                background-color: #e8f0fe;
                padding: 10px 15px;
                border-radius: 8px;
                margin-bottom: 20px;
                display: inline-block;
            }
            .tabla-container {
                background-color: white;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
                overflow-x: auto;        
            }

        </style>
    </head>
    <body>
        <div class="container mt-2">
            <h2 class="titulo mb-3">Medicamentos existentes</h2>
            <div id="contenedor_tabla" class="tabla-container">
              <!-- tabla medicamentos existentes -->
            </div>
        </div>

        <script src="js/consulta.js"></script>
    </body>
</html>
