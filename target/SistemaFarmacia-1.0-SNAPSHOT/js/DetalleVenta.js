/* * JS PARA GESTIONAR EL DETALLE DE LA VENTA
 */

// 1. CAPTURAMOS EL ID DE LA VENTA DESDE LA URL AL INICIAR
const params = new URLSearchParams(window.location.search);
const idVentaGlobal = params.get("idVenta");

// Referencias del DOM
const btnAddDetalle = document.querySelector("#registrar_Detalle");
const formularioDetalle = document.querySelector("#formulario_Detalle");
const comboMedicamento = document.querySelector("#idMedicamento");

document.addEventListener("DOMContentLoaded", () => {
    // Si no hay ID en la URL, alerta y regresa
    if (!idVentaGlobal) {
        Swal.fire('Error', 'No se ha seleccionado una venta', 'error')
            .then(() => window.location.href = "RegistrarVenta.jsp");
        return;
    }

    // Mostrar el ID en el título
    document.querySelector("#lblIdVenta").textContent = idVentaGlobal;
    // Asignar el ID al input oculto del formulario
    document.querySelector("#idVenta").value = idVentaGlobal;

    cargarCombosMedicamentos();
    cargarTablaDetalles();

    // Inicializar Select2 en el Modal
    $('#idMedicamento').select2({
        dropdownParent: $('#md_registrar_Detalle')
    });
    $('#formulario_Detalle').parsley();
});

// ABRIR MODAL PARA AGREGAR PRODUCTO
btnAddDetalle.addEventListener('click', () => {
    formularioDetalle.reset();
    $('#formulario_Detalle').parsley().reset();
    $('#idMedicamento').val("").trigger('change');
    
    // Asegurarnos que el ID de venta siga ahí
    document.querySelector("#idVenta").value = idVentaGlobal;
    document.querySelector("#opcion").value = "insertar";
    
    // Bootstrap 5 modal show
    var myModal = new bootstrap.Modal(document.getElementById('md_registrar_Detalle'));
    myModal.show();
});

// CARGAR LISTA DE MEDICAMENTOS (SERVLET)
const cargarCombosMedicamentos = () => {
    $.ajax({
        url: "DetalleServlt", // Asegúrate que este sea el nombre correcto en tu Servlet (@WebServlet)
        method: "POST",
        dataType: "json",
        data: { "opcion": "cargarCombos" }
    }).done(function (json) {
        // Verifica si json viene como array o objeto
        var data = Array.isArray(json) ? json[0] : json;
        if (data.resultado === "exito") {
            comboMedicamento.innerHTML += data.medicamentos;
        } else {
            console.error("Error cargando medicamentos");
        }
    });
};

// CARGAR TABLA DE DETALLES DE ESTA VENTA
const cargarTablaDetalles = () => {
    $.ajax({
        url: "DetalleServlt", // Asegúrate del nombre del Servlet
        method: "POST",
        dataType: "json",
        data: { 
            "opcion": "consultar",
            "idVenta": idVentaGlobal 
        }
    }).done(function (json) {
        var data = Array.isArray(json) ? json[0] : json;
        if (data.resultado === "exito") {
            $("#tablita").html(data.tabla);
            $("#detalle_registradas").text(data.cantidad);
            
            // Inicializar DataTable
            $("#tabla_Detalle").DataTable({
                "language": { "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json" }
            });
        }
    });
};

// GUARDAR DETALLE (INSERTAR)
formularioDetalle.addEventListener("submit", (e) => {
    e.preventDefault();
    if (!$('#formulario_Detalle').parsley().isValid()) return;

    const datos = $("#formulario_Detalle").serialize();
    
    $.ajax({
        url: "DetalleServlt",
        method: "POST",
        dataType: "json",
        data: datos
    }).done(function (json) {
        var data = Array.isArray(json) ? json[0] : json;
        
        // Manejo de errores específicos (Existencias, etc)
        if (data.resultado && data.resultado.startsWith("error")) {
            Swal.fire('Error', data.mensaje || 'Error al guardar', 'error');
            return;
        }

        if (data.resultado === "exito" || data.resultado === "true") { // Ajusta según lo que devuelva tu Java
            Swal.fire({
                icon: 'success',
                title: 'Producto Agregado',
                showConfirmButton: false,
                timer: 1000
            });
            
            // Cerrar modal (Bootstrap 5)
            var modalEl = document.getElementById('md_registrar_Detalle');
            var modal = bootstrap.Modal.getInstance(modalEl);
            modal.hide();
            
            cargarTablaDetalles();
        } else {
            Swal.fire('Info', 'No se pudo guardar el registro', 'info');
        }
    }).fail(function(jqXHR, textStatus) {
        console.error(textStatus);
        Swal.fire('Error', 'Error de conexión con el servidor', 'error');
    });
});

// ELIMINAR DETALLE
$(document).on("click", ".btn_eliminar", function (e) {
    e.preventDefault();
    const idElim = $(this).data("id");
    
    Swal.fire({
        title: '¿Quitar producto?',
        text: "Se eliminará de la lista",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, quitar'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: "DetalleServlt",
                method: "POST",
                dataType: "json",
                data: { "opcion": "eliminar", "idDetalle_venta": idElim }
            }).done(function(json) {
                var data = Array.isArray(json) ? json[0] : json;
                if (data.resultado === "exito") {
                    Swal.fire('Eliminado', '', 'success');
                    cargarTablaDetalles();
                }
            });
        }
    });
});

// FINALIZAR COMPRA (Volver a Ventas)
document.querySelector("#btnFinalizarCompra").addEventListener("click", () => {
    Swal.fire({
        title: '¿Finalizar Venta?',
        text: "Regresarás a la pantalla principal",
        icon: 'success',
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        confirmButtonText: 'Sí, finalizar'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = "RegistrarVenta.jsp";
        }
    });
});