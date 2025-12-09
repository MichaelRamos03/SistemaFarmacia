/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
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

// PARA CARGAR EL MODAL QUE PERMITE REGISTRAR EL USUARIO
btnAddPersona.addEventListener('click', () => {
    formularioVenta.reset();
    $('#formulario_Venta').parsley().reset();
    $('#id').val("").trigger('change');
    title.innerHTML = "<h5 class='modal-title'\n\
 id='exampleModalLabel'>Registro nueva Persona\n\
<br><sub> Todos los campos son obligatorios</sub>";
    $("#md_registrar_Venta").modal("show");
    console.log("entró a cargar modal ");
    $("#idVenta").hide(); // ESTE SE OCULTA PORQUE SE GENERA DE FORMA AUTOMÁTICA
    $("#labelVenta").hide(); // ESTE SE OCULTA PORQUE SE GENERA DE FORMA AUTOMÁTICA
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
            console.log("error Usuarios");
        }
    }).fail(function () {
    });
};

const cargarDatos = () => {
    mostrar_cargando("Procesando Solicitud",
            "Espere un momento mientras se obtiene la información solicitada");
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
            document.querySelector("#venta_registradas").textContent
                    = json[0].cantidad;
        } else {
            Swal.fire(
                    "Error",
                    "No se pudo completar la petición, intentelo más tarde",
                    "error"
                    );
        }
    }).fail(function () {
    });
};



/// PARA INSERTAR DATOS
formularioVenta.addEventListener("submit", (e) => {
    e.preventDefault();
    if (!$('#id').parsley().isValid() ||
            !$('#id').parsley().isValid()
            ) {
        e.preventDefault();
        return;
    }
    const datos = $("#formulario_Venta").serialize();
    console.log("DATOS A INSERTAR/MODIFICAR " + datos);
    console.log("document.querySelector opcion.value"
            + document.querySelector("#opcion").value);
    if (document.querySelector("#opcion").value === "insertar") { //INSERTAR         
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
                    title: 'venta Registrada',
                    showConfirmButton: false,
                    timer: 1000
                });
                formularioVenta.reset();
                $("#md_registrar_Venta").modal("hide");

                setTimeout(() => {
                    window.location.href = "detalleVenta.jsp?idVenta=" + idVenta;
                }, 1000);

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



// modificar

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
                    title: 'venta Actualizada',
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
        },
        willClose: () => {
        }
    }).then((result) => {
        if (result.dismiss === Swal.DismissReason.timer) {
        }
    });
}

document.addEventListener("click", (e) => {
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
                Swal.fire(
                        "Error",
                        "No se pudo completar la petición, intentelo más tarde",
                        "error"
                        );
            }
        }).fail(function () {
        }).always(function () {
        });
    }

    $(document).on("click", ".btn_eliminar", function (e) {
        e.preventDefault();
        Swal.fire({
            title: '¿Desea eliminar el registro?',
            text: 'Al continuar, no podrá ser revertido y los datos serán borrados completamente',
            showDenyButton: true,
            showCancelButton: false,
            confirmButton: 'si',
            denyButton: 'NO'
        }).then((result) => {
            if (result.isConfirmed) {
                eliminar($(this).attr('data-id'));
            } else if (result.isDenied) {
                Swal.fire("Opcion cancelada por el usuario", '', 'info');
            }
        });
    });
});

function eliminar(id) {
    mostrar_cargando("Procesando solicitud", "Espere mientras se eliminan los datos " + id);
    var datos = {"opcion": "eliminar", "idVenta": id};
    console.log("id a eliminar es: " + id);
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ventaServlet",
        data: datos
    }).done(function (json) {
        Swal.close();
        if (json[0].resultado === "exito") {
            Swal.fire(
                    'Excelente',
                    'El dato fue eliminado',
                    'success'
                    );
            cargarDatos();
        } else {
            Swal.fire(
                    'Error',
                    'No se pudo eliminar el dato intentelo más tarde',
                    'error'
                    );
        }
    }).fail(function () {
        console.log("Error al eliminar");
    }).always(function () {
        console.log("Error al eliminar");
    });
}







