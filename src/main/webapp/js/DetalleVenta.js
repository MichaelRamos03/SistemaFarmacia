/* JS DETALLE VENTA - CORREGIDO */

const params = new URLSearchParams(window.location.search);
const idVentaGlobal = params.get("idVenta");

const btnAddDetalle = document.querySelector("#registrar_Detalle");
const formularioDetalle = document.querySelector("#formulario_Detalle");
const comboMedicamento = document.querySelector("#idMedicamento");

document.addEventListener("DOMContentLoaded", () => {
    if (!idVentaGlobal) {
        Swal.fire('Error', 'No hay venta seleccionada', 'error').then(() => window.location.href = "RegistrarVenta.jsp");
        return;
    }
    document.querySelector("#lblIdVenta").textContent = idVentaGlobal;
    document.querySelector("#idVenta").value = idVentaGlobal;

    cargarCombosMedicamentos();
    cargarTablaDetalles();

    $('#idMedicamento').select2({ theme: 'bootstrap-5', dropdownParent: $('#md_registrar_Detalle'), width: '100%' });
    $('#formulario_Detalle').parsley();
});

// ABRIR MODAL (NUEVO)
btnAddDetalle.addEventListener('click', () => {
    formularioDetalle.reset();
    $('#formulario_Detalle').parsley().reset();
    $('#idMedicamento').val("").trigger('change');
    document.querySelector("#idVenta").value = idVentaGlobal;
    document.querySelector("#opcion").value = "insertar";
    
    // Titulo Normal
    document.querySelector(".modal-title").innerHTML = "<i class='bi bi-capsule'></i> Agregar Medicamento";
    
    new bootstrap.Modal(document.getElementById('md_registrar_Detalle')).show();
});

// CARGAR DATOS TABLA
const cargarTablaDetalles = () => {
    $.ajax({
        url: "DetalleServlt", method: "POST", dataType: "json",
        data: { "opcion": "consultar", "idVenta": idVentaGlobal }
    }).done(function (json) {
        let data = Array.isArray(json) ? json[0] : json;
        if (data.resultado === "exito") {
            $("#tablita").html(data.tabla);
            $("#detalle_registradas").text(data.cantidad);
            $("#tabla_Detalle").DataTable({ "language": { "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json" }, "responsive": true, "ordering": false });
        }
    });
};

const cargarCombosMedicamentos = () => {
    $.ajax({ url: "DetalleServlt", method: "POST", dataType: "json", data: { "opcion": "cargarCombos" } })
    .done(function (json) {
        let data = Array.isArray(json) ? json[0] : json;
        if (data.resultado === "exito") comboMedicamento.innerHTML += data.medicamentos;
    });
};

// GUARDAR (INSERTAR O EDITAR)
formularioDetalle.addEventListener("submit", (e) => {
    e.preventDefault();
    if (!$('#formulario_Detalle').parsley().isValid()) return;
    
    $.ajax({
        url: "DetalleServlt", method: "POST", dataType: "json", data: $("#formulario_Detalle").serialize()
    }).done(function (json) {
        let data = Array.isArray(json) ? json[0] : json;
        if (data.resultado === "exito") {
            Swal.fire({ icon: 'success', title: 'Guardado', showConfirmButton: false, timer: 1000 });
            bootstrap.Modal.getInstance(document.getElementById('md_registrar_Detalle')).hide();
            cargarTablaDetalles();
        } else {
            Swal.fire('Error', data.mensaje || 'Error al guardar', 'error');
        }
    });
});

// --- AQUÍ ESTÁ LA FUNCIÓN EDITAR QUE FALTABA ---
$(document).on("click", ".btn_editar", function () {
    let idDetalle = $(this).data("id");
    
    // Cambiamos titulo y opcion
    document.querySelector(".modal-title").innerHTML = "<i class='bi bi-pencil-square'></i> Editar Cantidad";
    document.querySelector("#opcion").value = "si_actualizalo";
    
    $.ajax({
        url: "DetalleServlt", method: "POST", dataType: "json",
        data: { "opcion": "editar_consultar", "idDetalle_venta": idDetalle }
    }).done(function (json) {
        let data = Array.isArray(json) ? json[0] : json;
        if (data.resultado === "exito") {
            // Llenar datos
            $("#idDetalle_venta").val(data.detalle.idDetalle_venta);
            $("#cantidadProducto").val(data.detalle.cantidadProducto);
            // Select2 necesita trigger change
            $("#idMedicamento").val(data.detalle.idMedicamento).trigger('change');
            
            // Abrir Modal
            new bootstrap.Modal(document.getElementById('md_registrar_Detalle')).show();
        }
    });
});

// ELIMINAR
$(document).on("click", ".btn_eliminar", function (e) {
    let id = $(this).data("id");
    Swal.fire({ title: '¿Quitar?', icon: 'warning', showCancelButton: true, confirmButtonText: 'Sí' })
    .then((result) => {
        if (result.isConfirmed) {
            $.ajax({ url: "DetalleServlt", method: "POST", dataType: "json", data: { "opcion": "eliminar", "idDetalle_venta": id } })
            .done(function (json) {
                let data = Array.isArray(json) ? json[0] : json;
                if (data.resultado === "exito") { cargarTablaDetalles(); Swal.fire('Eliminado', '', 'success'); }
            });
        }
    });
});

// FINALIZAR
document.querySelector("#btnFinalizarCompra").addEventListener("click", () => {
    window.location.href = "RegistrarVenta.jsp";
});