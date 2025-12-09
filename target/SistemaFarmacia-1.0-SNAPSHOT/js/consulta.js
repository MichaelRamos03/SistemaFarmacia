$(document).ready(function () {
    cargarDatos();
});

function cargarDatos() {
    const datos = { opcion: "consultar" };

    $.ajax({
        method: "POST",
        url: "consultaServelet",
        data: datos,
        dataType: "json"
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            // 1. Insertar la tabla
            $("#contenedor_tabla").html(json[0].tabla);

            // 2. Inicializar DataTables con estilo Bootstrap 5 y Sin Error CORS
            $("#tabla").DataTable({
                responsive: true,
                language: {
                    "decimal": "",
                    "emptyTable": "No hay información disponible",
                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                    "infoEmpty": "Mostrando 0 to 0 of 0 registros",
                    "infoFiltered": "(Filtrado de _MAX_ total registros)",
                    "infoPostFix": "",
                    "thousands": ",",
                    "lengthMenu": "Mostrar _MENU_ registros",
                    "loadingRecords": "Cargando...",
                    "processing": "Procesando...",
                    "search": "Buscar:",
                    "zeroRecords": "No se encontraron resultados",
                    "paginate": {
                        "first": "Primero",
                        "last": "Último",
                        "next": "Siguiente",
                        "previous": "Anterior"
                    }
                },
                // Diseño de controles Bootstrap 5
                dom: '<"d-flex justify-content-between align-items-center mb-3"lf>rt<"d-flex justify-content-between align-items-center mt-3"ip>'
            });

        } else {
            $("#contenedor_tabla").html(`
                <div class="alert alert-warning text-center">
                    <i class="bi bi-exclamation-circle"></i> No se pudo cargar la información.
                </div>
            `);
        }
    }).fail(function () {
        $("#contenedor_tabla").html(`
            <div class="alert alert-danger text-center">
                <i class="bi bi-wifi-off"></i> Error de conexión con el servidor.
            </div>
        `);
    });
}