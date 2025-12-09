/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


const btnAddUsuario = document.querySelector("#registrar_usuario");
const formularioUsuario = document.querySelector("#formulario_usuario");
const comboRol = document.querySelector("#nombreRol");
const title = document.querySelector("#exampleModalLabel");
const error = document.querySelector("#error");
document.addEventListener("DOMContentLoaded", () => {
    cargarCombos();
    cargarDatos();

    $('#nombreRol').select2({
        dropdownParent: $('#md_registrar_usuario')
    });
    $('#formulario_usuario').parsley();
});

// PARA CARGAR EL MODAL QUE PERMITE REGISTRAR EL USUARIO
btnAddUsuario.addEventListener('click', () => {
    formularioUsuario.reset();
    $('#formulario_usuario').parsley().reset();
    $('#nombreRol').val("").trigger('change');
    title.innerHTML = "<h5 class='modal-title'\n\
 id='exampleModalLabel'>Registro nuevo Usuario\n\
<br><sub> Todos los campos son obligatorios</sub>";
    $("#md_registrar_usuario").modal("show");
    console.log("entró a cargar modal ");
    $("#idUsuario").hide(); // ESTE SE OCULTA PORQUE SE GENERA DE FORMA AUTOMÁTICA
    $("#labelIdUsuario").hide(); // ESTE SE OCULTA PORQUE SE GENERA DE FORMA AUTOMÁTICA
});
const cargarCombos = () => {
    var datos = {"opcion": "cargarCombos"};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "usuarioServlt",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            comboRol.innerHTML += json[0].roles;
            console.log(json[0].roles);
        } else {
            console.log("error roles");
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
        url: "usuarioServlt",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#tablita").empty().html(json[0].tabla);
            $("#tabla_usuarios").DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
                }
            });
            document.querySelector("#usuarios_registrados").textContent
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
formularioUsuario.addEventListener("submit", (e) => {
    e.preventDefault();
    if (!$('#nombreRol').parsley().isValid() ||
            !$('#nombreRol').parsley().isValid()
            ) {
        e.preventDefault();
        return;
    }
    const datos = $("#formulario_usuario").serialize();
    console.log("DATOS A INSERTAR/MODIFICAR " + datos);
    console.log("document.querySelector opcion.value"
            + document.querySelector("#opcion").value);
    if (document.querySelector("#opcion").value === "insertar") { //INSERTAR         
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "usuarioServlt",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                Swal.fire({
                    icon: 'success',
                    title: 'Usuario Registrado',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioUsuario.reset();
                $("#md_registrar_usuario").modal("hide");
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
            url: "usuarioServlt",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#opcion").val("insertar");
                Swal.fire({
                    icon: 'success',
                    title: 'Usuario Actualizado',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioUsuario.reset();
                $("#md_registrar_usuario").modal("hide");
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
        $("#idUsuario").show();
        $("#labelIdUsuario").show();
        $('#formulario_usuario').parsley().reset();
        title.innerHTML = "<h5 class='modal-title' id='exampleModalLabel'>Editar Usuario<br><sub> Todos los campos son obligatorios</sub>";
        const id = e.target.getAttribute("data-id");
        var datos = {"opcion": "editar_consultar", "idUsuario": id};
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "usuarioServlt",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                document.querySelector("#idUsuario").value = json[0].usuario.idUsuario;
                document.querySelector("#Usuario").value = json[0].usuario.Usuario;
                document.querySelector("#nombreRol").value = json[0].usuario.nombreRol;
                document.querySelector("#contrasenia").value = json[0].usuario.contrasenia;
                document.querySelector(`input[name="estado"][value="${json[0].usuario.estado}"]`).checked = true;

            document.querySelector("#idUsuario").readOnly = true;
                $('#nombreRol').val(json[0].usuario.nombreRol).trigger('change');
                $("#md_registrar_usuario").modal("show");
                $("#opcion").val("actualizar");
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
    var datos = {"opcion": "eliminar", "idUsuario": id};
    console.log("id a eliminar es: " + id);
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "usuarioServlt",
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

$(document).ready(function () {
  
    $("#mostrar_inactivas").on("click", function () {
        const contenedor = $("#contenedor_inactivas");
        contenedor.slideToggle();

        if (contenedor.is(":visible")) {
            $.ajax({
                method: "POST",
                url: "usuarioServlt",
                data: { opcion: "listar_inactivas" }, 
                dataType: "json"
            }).done(function (data) {
                const lista = $("#lista_usuarios_inactivas");
                lista.empty();

                if (data.length === 0) {
                    lista.append("<p>No hay categorías inactivas.</p>");
                } else {
                    data.forEach(cat => {
                        lista.append(`
                            <div class="d-flex justify-content-between align-items-center border-bottom py-2">
                                <div>
                                    <strong>${cat.Usuario}</strong><br>
                                    <small>${cat.contrasenia}</small>
                                </div>
                                <button class="btn btn-su${cat.Usuario}</strong><br>
                                    <small>${cat.contrasenia}</ccess btn-sm reactivar-usuario" data-id="${cat.idUsuario}">
                                    Reactivar
                                </button>
                            </div>
                        `);
                    });
                }
            }).fail(() => {
                $("#lista_usuarios_inactivas").html("<p>Error al cargar los usuarios inactivos.</p>");
            });
        }
    });

   
});

$(document).on("click", ".reactivar-usuario", function (e) {
    e.preventDefault();

    const idUsuario = $(this).data("id");

    Swal.fire({
        title: "¿Reactivar usuario?",
        text: "Esta acción reactivará el usuario seleccionado.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, reactivar",
        cancelButtonText: "Cancelar",
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: "usuarioServlt",
                method: "POST",
                data: {
                    opcion: "reactivar",
                    idUsuario: idUsuario
                },
                success: function (response) {
                    if (response[0]?.resultado === "exito") {
                        Swal.fire({
                            icon: "success",
                            title: "Reactivado",
                            text: "El usuario fue reactivado exitosamente",
                            showConfirmButton: false,
                            timer: 1500
                        });

                        setTimeout(() => {
                            location.reload();
                        }, 1600);

                    } else {
                        Swal.fire("Error", "No se pudo reactivar el usuario", "error");
                    }
                },
                error: function () {
                    Swal.fire("Error", "Error en la petición AJAX", "error");
                }
            });
        }
    });
});





