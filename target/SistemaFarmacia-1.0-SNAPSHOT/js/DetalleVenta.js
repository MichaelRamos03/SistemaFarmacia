/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
const btnAddDetalle = document.querySelector("#registrar_Detalle");
const formularioDetalle = document.querySelector("#formulario_Detalle");
const comboMedicamento = document.querySelector("#idMedicamento");
const title = document.querySelector("#exampleModalLabel");
const error = document.querySelector("#error");

document.addEventListener("DOMContentLoaded", () => {
    cargarCombos();
    cargarDatos();

    const urlParams = new URLSearchParams(window.location.search);
    const idVenta = urlParams.get("idVenta");
    if (idVenta) {
        document.querySelector("#idVenta").value = idVenta;
        console.log("idVenta cargado desde URL:", idVenta);
    } else {
        console.warn("No se encontró idVenta en la URL");
    }

    $('#idMedicamento').select2({
        dropdownParent: $('#md_registrar_Detalle')


    });

    $('#formulario_Detalle').parsley();
});

// PARA CARGAR EL MODAL QUE PERMITE REGISTRAR EL USUARIO
btnAddDetalle.addEventListener('click', () => {
    formularioDetalle.reset();
    $('#formulario_Detalle').parsley().reset();
    $('#idMedicamento').val("").trigger('change');
    title.innerHTML = "<h5 class='modal-title'\n\
 id='exampleModalLabel'>Registro nuevo detalle\n\
<br><sub> Todos los campos son obligatorios</sub>";
    $("#md_registrar_Detalle").modal("show");
    console.log("entró a cargar modal ");
    $("#idDetalle_venta").hide(); // ESTE SE OCULTA PORQUE SE GENERA DE FORMA AUTOMÁTICA
    $("#labelDetalle").hide(); // ESTE SE OCULTA PORQUE SE GENERA DE FORMA AUTOMÁTICA
});
const cargarCombos = () => {
    var datos = {"opcion": "cargarCombos"};
    console.log(datos);



    $.ajax({
        dataType: "json",
        method: "POST",
        url: "DetalleServlt",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            comboMedicamento.innerHTML += json[0].medicamentos;
            console.log(json[0].medicamentos);


        } else {
            console.log("error combos");
        }
    }).fail(function () {
    });
};

const cargarDatos = () => {
    mostrar_cargando("Procesando Solicitud",
            "Espere un momento mientras se obtiene la información solicitada");

    const idVenta = document.querySelector("#idVenta").value;

    const datos = {
        "opcion": "consultar",
        "idVenta": idVenta
    };

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "DetalleServlt",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#tablita").empty().html(json[0].tabla);

            $("#tabla_Detalle").DataTable({
                "language": {
                    "url": "https://cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
                }
            });
            document.querySelector("#detalle_registradas").textContent
                    = json[0].cantidad;

            document.querySelector("#total_general").textContent
                    = "$" + json[0].total;
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
formularioDetalle.addEventListener("submit", (e) => {
    e.preventDefault();
    if (!$('#idMedicamento').parsley().isValid() ||
            !$('#idMedicamento').parsley().isValid()
            ) {
        e.preventDefault();
        return;
    }


    const datos = $("#formulario_Detalle").serialize();
    console.log("DATOS A INSERTAR/MODIFICAR " + datos);
    console.log("document.querySelector opcion.value"
            + document.querySelector("#opcion").value);
    if (document.querySelector("#opcion").value === "insertar") {
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "DetalleServlt",
            data: datos
        }).done(function (json) {
              if (json[0].resultado === "exito") {
                Swal.fire({
                    icon: 'success',
                    title: 'Detalle Registrado',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioDetalle.reset();
                $("#md_registrar_Detalle").modal("hide");
                setTimeout(() => {
                    cargarDatos();
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



// modificar

    } else {
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "DetalleServlt",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#opcion").val("insertar");
                Swal.fire({
                    icon: 'success',
                    title: 'detalle Actualizado',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioDetalle.reset();
                $("#md_registrar_Detalle").modal("hide");
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
        $("#idDetalle_venta").show();
        $("#labelDetalle").show();
        $('#formulario_Detalle').parsley().reset();
        title.innerHTML = "<h5 class='modal-title' id='exampleModalLabel'>Editar Persona<br><sub> Todos los campos son obligatorios</sub>";
        const id = e.target.getAttribute("data-id");
        var datos = {"opcion": "editar_consultar", "idDetalle_venta": id};
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "DetalleServlt",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                document.querySelector("#idDetalle_venta").value = json[0].detalleVenta.idDetalle_venta;
                document.querySelector("#cantidadProducto").value = json[0].detalleVenta.cantidadProducto;

                document.querySelector("#idMedicamento").value = json[0].detalleVenta.idMedicamento;
                document.querySelector("#idVenta").value = json[0].detalleVenta.idVenta;

                document.querySelector("#idDetalle_venta").readOnly = true;
                $('#idMedicamento').val(json[0].detalleVenta.idMedicamento).trigger('change');
                $("#md_registrar_Detalle").modal("show");
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
    var datos = {"opcion": "eliminar", "idDetalle_venta": id};
    console.log("id a eliminar es: " + id);
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "DetalleServlt",
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







