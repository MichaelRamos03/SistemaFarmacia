/* * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

const btnAddPersona = document.querySelector("#registrar_Venta");
const formularioVenta = document.querySelector("#formulario_Venta");
const combo = document.querySelector("#id");
const title = document.querySelector("#exampleModalLabel");
const error = document.querySelector("#error");

document.addEventListener("DOMContentLoaded", () => {
    cargarCombos();
    cargarDatos();

    $('#id').select2({
        dropdownParent: $('#md_registrar_Venta')
    });
    $('#formulario_Venta').parsley();
});

// PARA CARGAR EL MODAL QUE PERMITE REGISTRAR LA VENTA
btnAddPersona.addEventListener('click', () => {
    formularioVenta.reset();
    $('#formulario_Venta').parsley().reset();
    $('#id').val("").trigger('change');
    title.innerHTML = "<h5 class='modal-title' id='exampleModalLabel'>Registro nueva Venta<br><sub> Todos los campos son obligatorios</sub>";
    $("#md_registrar_Venta").modal("show");
    console.log("entró a cargar modal ");
    $("#idVenta").hide(); 
    $("#labelVenta").hide(); 
});

const cargarCombos = () => {
    var datos = {"opcion": "cargarCombos"};
    console.log(datos);

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ventaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            combo.innerHTML += json[0].persona;
            console.log(json[0].persona);
        } else {
            console.log("error al cargar combos");
        }
    }).fail(function () {
    });
};

const cargarDatos = () => {
    mostrar_cargando("Procesando Solicitud", "Espere un momento mientras se obtiene la información solicitada");
    const datos = {"opcion": "consultar"};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ventaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#tablita").empty().html(json[0].tabla);

            $("#tabla_venta").DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
                }
            });
            document.querySelector("#venta_registradas").textContent = json[0].cantidad;
        } else {
            Swal.fire("Error", "No se pudo completar la petición, inténtelo más tarde", "error");
        }
    }).fail(function () {
    });
};

/// PARA INSERTAR O EDITAR DATOS
formularioVenta.addEventListener("submit", (e) => {
    e.preventDefault();
    if (!$('#id').parsley().isValid()) {
        e.preventDefault();
        return;
    }
    const datos = $("#formulario_Venta").serialize();
    console.log("DATOS A INSERTAR/MODIFICAR " + datos);
    
    // --- INSERTAR NUEVA VENTA ---
    if (document.querySelector("#opcion").value === "insertar") { 
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "ventaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                const idVenta = json[0].idVenta;
                
                Swal.fire({
                    icon: 'success',
                    title: 'Venta Registrada',
                    text: 'Redirigiendo a agregar productos...',
                    showConfirmButton: false,
                    timer: 1500
                });
                
                formularioVenta.reset();
                $("#md_registrar_Venta").modal("hide");

                setTimeout(() => {
                    // === AQUÍ ESTÁ LA CORRECCIÓN ===
                    // Redirige a la pantalla de detalles pasando el ID de la venta
                    window.location.href = "detalleVenta.jsp?idVenta=" + idVenta;
                }, 1500);

            } else {
                Swal.fire({
                    icon: 'info',
                    title: 'No se logró insertar el registro',
                    showConfirmButton: false,
                    timer: 1500
                });
            }
        }).fail(function () {
        });

    // --- MODIFICAR VENTA ---
    } else {
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "ventaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#opcion").val("insertar");
                Swal.fire({
                    icon: 'success',
                    title: 'Venta Actualizada',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioVenta.reset();
                $("#md_registrar_Venta").modal("hide");
                setTimeout(() => {
                    cargarDatos();
                }, 1500);
            } else {
                Swal.fire({
                    icon: 'info',
                    title: 'No se logró actualizar el registro',
                    showConfirmButton: false,
                    timer: 1500
                });
            }
        }).fail(function () {
        });
    }
});

function mostrar_cargando(titulo, mensaje = "") {
    Swal.fire({
        title: titulo,
        html: mensaje,
        timer: 2000,
        timerProgressBar: true,
        didOpen: () => {
            Swal.showLoading();
        }
    });
}

document.addEventListener("click", (e) => {
    // BOTÓN EDITAR
    if (e.target.classList.contains("btn_editar")) {
        $("#idVenta").show();
        $("#labelVenta").show();
        $('#formulario_Venta').parsley().reset();
        title.innerHTML = "<h5 class='modal-title' id='exampleModalLabel'>Editar Venta<br><sub> Todos los campos son obligatorios</sub>";
        const id = e.target.getAttribute("data-id");
        var datos = {"opcion": "editar_consultar", "idVenta": id};
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "ventaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                document.querySelector("#idVenta").value = json[0].venta.idVenta;
                document.querySelector("#fechaVenta").value = json[0].venta.fechaVenta;
                document.querySelector("#id").readOnly = true;
                $('#id').val(json[0].venta.id).trigger('change');
                $("#md_registrar_Venta").modal("show");
                $("#opcion").val("si_actualizalo");
            } else {
                Swal.fire("Error", "No se pudo completar la petición", "error");
            }
        }).fail(function () {});
    }

    // BOTÓN ELIMINAR
    if (e.target.classList.contains("btn_eliminar")) {
        e.preventDefault();
        const idEliminar = e.target.getAttribute("data-id"); 
        
        Swal.fire({
            title: '¿Desea eliminar el registro?',
            text: 'Al continuar, no podrá ser revertido',
            showDenyButton: true,
            confirmButtonText: 'Si',
            denyButtonText: 'No'
        }).then((result) => {
            if (result.isConfirmed) {
                eliminar(idEliminar);
            }
        });
    }
});

function eliminar(id) {
    mostrar_cargando("Procesando solicitud", "Espere mientras se eliminan los datos " + id);
    var datos = {"opcion": "eliminar", "idVenta": id};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ventaServlet",
        data: datos
    }).done(function (json) {
        Swal.close();
        if (json[0].resultado === "exito") {
            Swal.fire('Excelente', 'El dato fue eliminado', 'success');
            cargarDatos();
        } else {
            Swal.fire('Error', 'No se pudo eliminar el dato', 'error');
        }
    }).fail(function () {
        console.log("Error al eliminar");
    });
}

// =========================================================
// FUNCIÓN PARA IMPRIMIR FACTURA (VISTA PREVIA)
// =========================================================
function imprimirFactura(idVenta, tipo) {
    
    // 1. Mostrar mensaje de carga
    Swal.fire({
        title: 'Generando Vista Previa',
        text: 'Procesando documento ' + tipo.toUpperCase() + '...',
        allowOutsideClick: false,
        didOpen: () => { Swal.showLoading(); }
    });

    // 2. Pedir la factura al Servlet
    $.ajax({
        url: 'ventaServlet',
        data: {
            opcion: 'generarFactura',
            idVenta: idVenta,
            tipoDocumento: tipo
        },
        type: 'POST',
        dataType: 'json',
        success: function (data) {
            Swal.close();
            
            // Aseguramos que data sea el objeto correcto
            var response = (Array.isArray(data)) ? data[0] : data;

            if (response.resultado === "exito") {
                // 3. Abrir ventana POPUP
                var ventana = window.open('', 'VISTA_PREVIA', 'height=800,width=900,scrollbars=yes');
                
                // Escribir el HTML generado por el servidor
                ventana.document.write(response.html);
                ventana.document.close(); 
                ventana.focus();
                
                // NOTA: Se eliminó el window.print() automático para que solo muestre la vista previa
                
            } else {
                Swal.fire('Error', response.mensaje || 'Error desconocido al generar factura', 'error');
            }
        },
        error: function (jqXHR) {
            Swal.close();
            Swal.fire('Error', 'No se pudo conectar con el servidor', 'error');
            console.error(jqXHR.responseText);
        }
    });
}