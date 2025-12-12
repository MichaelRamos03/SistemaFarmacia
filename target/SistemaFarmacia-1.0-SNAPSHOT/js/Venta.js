/* * Funciones JS para Ventas - Diseño Farmacia
 */

const formularioVenta = document.querySelector("#formulario_Venta");
const title = document.querySelector("#exampleModalLabel");

document.addEventListener("DOMContentLoaded", () => {
    cargarCombos();
    cargarDatos();
    $('#formulario_Venta').parsley();
});

// ABRIR MODAL
$("#registrar_Venta").on('click', function() {
    formularioVenta.reset();
    $('#formulario_Venta').parsley().reset();
    $('#id').val("").trigger('change');
    
    // Cambiar título con Icono
    $("#exampleModalLabel").html("<i class='bi bi-cart-plus-fill'></i> Registro Nueva Venta");
    
    $("#md_registrar_Venta").modal("show");
    $("#grupoIdVenta").hide(); 
    $("#opcion").val("insertar");
});

const cargarCombos = () => {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ventaServlet",
        data: {"opcion": "cargarCombos"}
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#id").html(json[0].persona);
        }
    });
};

const cargarDatos = () => {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ventaServlet",
        data: {"opcion": "consultar"}
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#tablita").empty().html(json[0].tabla);
            
            // Inicializar DataTables BS5
            $("#tabla_venta").DataTable({
                "language": { "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json" },
                "responsive": true,
                "autoWidth": false
            });
            $("#venta_registradas").text(json[0].cantidad);
        } else {
            Swal.fire("Error", "No se pudo cargar la tabla", "error");
        }
    });
};

// SUBMIT FORMULARIO
formularioVenta.addEventListener("submit", (e) => {
    e.preventDefault();
    if (!$('#formulario_Venta').parsley().isValid()) return;

    const datos = $("#formulario_Venta").serialize();
    const opcion = $("#opcion").val();

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ventaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            Swal.fire({
                icon: 'success',
                title: (opcion === "insertar") ? 'Venta Registrada' : 'Venta Actualizada',
                showConfirmButton: false,
                timer: 1500
            });
            
            $("#md_registrar_Venta").modal("hide");

            if (opcion === "insertar") {
                setTimeout(() => {
                    // Redirigir a detalles
                    window.location.href = "detalleVenta.jsp?idVenta=" + json[0].idVenta;
                }, 1500);
            } else {
                cargarDatos();
            }
        } else {
            Swal.fire("Info", "No se pudo completar la operación", "info");
        }
    });
});

// BOTONES ACCIÓN (Delegación de eventos jQuery)
$(document).on('click', '.btn_editar', function () {
    let id = $(this).attr('data-id');
    $("#grupoIdVenta").show();
    $('#formulario_Venta').parsley().reset();
    $("#exampleModalLabel").html("<i class='bi bi-pencil-square'></i> Editar Venta");
    $("#opcion").val("si_actualizalo");

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ventaServlet",
        data: {"opcion": "editar_consultar", "idVenta": id}
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#idVenta").val(json[0].venta.idVenta);
            $("#fechaVenta").val(json[0].venta.fechaVenta);
            $('#id').val(json[0].venta.id).trigger('change');
            $("#md_registrar_Venta").modal("show");
        }
    });
});

$(document).on('click', '.btn_eliminar', function () {
    let id = $(this).attr('data-id');
    Swal.fire({
        title: '¿Eliminar registro?',
        text: "No se podrá revertir esta acción",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            eliminar(id);
        }
    });
});

function eliminar(id) {
    // Mostramos cargando para que el usuario sepa que algo pasa
    Swal.fire({
        title: 'Procesando...',
        text: 'Intentando eliminar el registro',
        allowOutsideClick: false,
        didOpen: () => { Swal.showLoading(); }
    });

    var datos = {"opcion": "eliminar", "idVenta": id};
    
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ventaServlet",
        data: datos
    }).done(function (json) {
        Swal.close(); // Cerramos el loading
        
        // Verificamos la respuesta del Servlet
        if (json[0].resultado === "exito") {
            Swal.fire(
                '¡Eliminado!',
                'La venta ha sido eliminada correctamente.',
                'success'
            );
            cargarDatos(); // Recargamos la tabla
            
        } else if (json[0].resultado === "error_integridad") {
            // MENSAJE ESPECÍFICO PARA TU PROBLEMA
            Swal.fire(
                'No se puede eliminar',
                'Esta venta contiene productos/detalles asociados. <br>Debes eliminar los productos de la venta primero.',
                'warning'
            );
        } else {
            Swal.fire(
                'Error',
                'No se pudo eliminar el registro. Verifique que no tenga datos asociados.',
                'error'
            );
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        Swal.close();
        // Ahora si falla el servidor (Error 500), te avisará
        console.error("Error servidor:", errorThrown);
        Swal.fire(
            'Error Crítico',
            'Ocurrió un error de conexión con el servidor (Error 500). Revisa la consola.',
            'error'
        );
    });
}

// IMPRIMIR FACTURA
function imprimirFactura(idVenta, tipo) {
    Swal.fire({
        title: 'Generando Documento',
        text: 'Procesando ' + tipo.toUpperCase() + '...',
        allowOutsideClick: false,
        didOpen: () => { Swal.showLoading(); }
    });

    $.ajax({
        url: 'ventaServlet',
        data: {
            opcion: 'generarFactura',
            idVenta: idVenta,
            tipoDocumento: tipo
        },
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            Swal.close();
            // Validar si viene en array o objeto directo
            var data = (Array.isArray(response)) ? response[0] : response;

            if (data.resultado === "exito") {
                var ventana = window.open('', 'VISTA_PREVIA', 'height=800,width=900,scrollbars=yes');
                ventana.document.write(data.html);
                ventana.document.close();
                ventana.focus();
            } else {
                Swal.fire('Error', data.mensaje || 'Error al generar', 'error');
            }
        },
        error: function () {
            Swal.close();
            Swal.fire('Error', 'Error de conexión', 'error');
        }
    });
}