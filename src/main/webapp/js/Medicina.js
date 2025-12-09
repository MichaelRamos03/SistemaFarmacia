/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */



const btnAddPersona = document.querySelector("#registrar_medicina");
const formularioPersona = document.querySelector("#formulario_medicina");
const combo = document.querySelector("#idCategoria");
const title = document.querySelector("#exampleModalLabel");
const error = document.querySelector("#error");

document.addEventListener("DOMContentLoaded", () => {
    cargarCombos();
    cargarDatos();

    $('#idCategoria').select2({
        dropdownParent: $('#md_registrar_medicina')
    });
    $('#formulario_medicina').parsley();
});

// PARA CARGAR EL MODAL QUE PERMITE REGISTRAR EL USUARIO
btnAddPersona.addEventListener('click', () => {
    formularioPersona.reset();
    $('#formulario_medicina').parsley().reset();
    $('#idCategoria').val("").trigger('change');
    title.innerHTML = "<h5 class='modal-title'\n\
 id='exampleModalLabel'>Registro nuevo Medicamento\n\
<br><sub> Todos los campos son obligatorios</sub>";
    $("#md_registrar_medicina").modal("show");
    console.log("entró a cargar modal ");
    $("#idMedicamento").hide(); // ESTE SE OCULTA PORQUE SE GENERA DE FORMA AUTOMÁTICA
    $("#labelMedicina").hide(); // ESTE SE OCULTA PORQUE SE GENERA DE FORMA AUTOMÁTICA
});

const cargarCombos = () => {
    var datos = {"opcion": "cargarCombos"};
    console.log(datos);

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "medicinaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            combo.innerHTML += json[0].categoria;
            console.log(json[0].categoria);
        } else {
            console.log("error categoria");
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
        url: "medicinaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#tablita").empty().html(json[0].tabla);

            $("#tabla_medicamentos").DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
                }
            });
            document.querySelector("#medicina_registradas").textContent
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
formularioPersona.addEventListener("submit", (e) => {
    e.preventDefault();
    if (!$('#idCategoria').parsley().isValid() ||
            !$('#idCategoria').parsley().isValid()
            ) {
        e.preventDefault();
        return;
    }
    const datos = $("#formulario_medicina").serialize();
    console.log("DATOS A INSERTAR/MODIFICAR " + datos);
    console.log("document.querySelector opcion.value"
            + document.querySelector("#opcion").value);
    if (document.querySelector("#opcion").value === "insertar") { //INSERTAR         
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "medicinaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                Swal.fire({
                    icon: 'success',
                    title: 'Persona Registrada',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioPersona.reset();
                $("#md_registrar_medicina").modal("hide");
                setTimeout(() => {
                    cargarDatos();
                }, 1500);
            } else {
                Swal.fire({
                    icon: 'info',
                    title: 'No se logró insertar el registro (registro duplicado)' ,
                    
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
            url: "medicinaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#opcion").val("insertar");
                Swal.fire({
                    icon: 'success',
                    title: 'Persona Actualizada',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioPersona.reset();
                $("#md_registrar_medicina").modal("hide");
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
        $("#idMedicamento").show();
        $("#labelMedicina").show();
        $('#formulario_medicina').parsley().reset();
        title.innerHTML = "<h5 class='modal-title' id='exampleModalLabel'>Editar Persona<br><sub> Todos los campos son obligatorios</sub>";
        const id = e.target.getAttribute("data-id");
        var datos = {"opcion": "editar_consultar", "idMedicamento": id};
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "medicinaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                document.querySelector("#idMedicamento").value = json[0].medicina.idMedicamento;
                document.querySelector("#nombre").value = json[0].medicina.nombre;
                document.querySelector("#cantidadExistencias").value = json[0].medicina.cantidadExistencias;
               document.querySelector("#precioUnidad").value = json[0].medicina.precioUnidad;
                document.querySelector("#fechaIngreso").value = json[0].medicina.fechaIngreso;
                document.querySelector("#fechaDeExpiracion").value = json[0].medicina.fechaDeExpiracion;
                document.querySelector("#descripcion").value = json[0].medicina.descripcion;
                document.querySelector(`input[name="activo"][value="${json[0].medicina.activo}"]`).checked = true;
               
             
                document.querySelector("#idCategoria").readOnly = true;
                
                $('#idCategoria').val(json[0].medicina.idCategoria).trigger('change');
                $("#md_registrar_medicina").modal("show");
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
});


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

function eliminar(id) {
    mostrar_cargando("Procesando solicitud", "Espere mientras se eliminan los datos " + id);
    var datos = {"opcion": "eliminar", "idMedicamento": id};
    console.log("id a eliminar es: " + id);
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "medicinaServlet",
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







