<%-- 
    Document   : ConsultaExistentes
    Created on : 21 may 2025, 4:31:37 p. m.
    Author     : Gaby Laínez
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consulta de Existencias</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/dataTables.bootstrap5.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.4.1/css/responsive.bootstrap5.min.css">

    <link rel="stylesheet" href="css/estilosUsuario.css">
</head>
<body class="bg-light">

    <div class="container-fluid p-4">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold text-dark-purple">
                <i class="bi bi-clipboard-data-fill"></i> Consulta de Existencias
            </h2>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="Menu.jsp">Inicio</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Consultas</li>
                </ol>
            </nav>
        </div>

        <div class="row mb-4">
            <div class="col-12">
                <div class="alert alert-custom bg-white shadow-sm border-0 border-start border-5 border-primary d-flex align-items-center p-4">
                    <div class="rounded-circle bg-primary-soft text-primary p-3 me-3">
                        <i class="bi bi-info-circle-fill fs-3"></i>
                    </div>
                    <div>
                        <h5 class="fw-bold mb-1 text-dark-purple">Resumen de Inventario</h5>
                        <p class="mb-0 text-muted">Aquí puede visualizar la disponibilidad actual de todos los medicamentos registrados en el sistema.</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="card shadow border-0 rounded-3">
            <div class="card-header bg-white py-3 border-bottom">
                <h5 class="mb-0 text-secondary fw-bold">Listado Detallado</h5>
            </div>
            <div class="card-body">
                <div class="table-responsive" id="contenedor_tabla">
                    <div class="text-center py-5">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Cargando...</span>
                        </div>
                        <p class="mt-2 text-muted">Consultando existencias...</p>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.4/js/dataTables.bootstrap5.min.js"></script>

    <script src="js/consulta.js"></script>
</body>
</html>