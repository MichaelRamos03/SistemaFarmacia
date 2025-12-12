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

    // Select2 con tema Bootstrap 5
    $('#nombreRol').select2({
        dropdownParent: $('#md_registrar_usuario'),
        theme: 'bootstrap-5',
        width: '100%'
    });

    $('#formulario_usuario').parsley();
});

// ABRIR MODAL REGISTRO
btnAddUsuario.addEventListener('click', () => {
    formularioUsuario.reset();
    $('#formulario_usuario').parsley().reset();
    $('#nombreRol').val("").trigger('change');
    
    // Titulo dinámico con Icono
    title.innerHTML = '<i class="bi bi-person-plus-fill"></i> Registro Nuevo Usuario';
    
    // Usamos la API de Bootstrap 5 para mostrar modal
    const myModal = new bootstrap.Modal(document.getElementById('md_registrar_usuario'));
    myModal.show();
    
    $("#divIdUsuario").hide(); // Ocultamos el div contenedor del ID
});

const cargarCombos = () => {
    var datos = { "opcion": "cargarCombos" };
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "usuarioServlt",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            comboRol.innerHTML += json[0].roles;
        } else {
            console.log("error roles");
        }
    }).fail(function () {
        console.log("Error al cargar combos");
    });
};

const cargarDatos = () => {
    // Spinner de carga manual si se desea
    const datos = { "opcion": "consultar" };
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "usuarioServlt",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#tablita").empty().html(json[0].tabla);
            
            // Inicializar DataTables con diseño Bootstrap 5
            $("#tabla_usuarios").DataTable({
                responsive: true,
                language: {
                    url: "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json"
                },
                dom: '<"d-flex justify-content-between align-items-center mb-3"lf>rt<"d-flex justify-content-between align-items-center mt-3"ip>'
            });

            document.querySelector("#usuarios_registrados").textContent = json[0].cantidad;
        } else {
            Swal.fire("Error", "No se pudo cargar la tabla", "error");
        }
    }).fail(function () {
        console.log("Error en petición consultar");
    });
};

// INSERTAR O EDITAR
$(document).on("submit", "#formulario_usuario", function (e) {
    e.preventDefault();
    
    // Validación manual de Parsley
    const formInstance = $(this).parsley();
    if (!formInstance.isValid()) {
        return;
    }

    const datos = $(this).serialize();
    const opcion = document.querySelector("#opcion").value;

    if (opcion === "insertar") {
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "usuarioServlt",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                Swal.fire({
                    icon: 'success',
                    title: '¡Registrado!',
                    text: 'Usuario creado correctamente',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioUsuario.reset();
                $('#md_registrar_usuario').modal('hide'); // Cerrar modal BS5
                $('.modal-backdrop').remove(); // Limpiar backdrop por si acaso
                cargarDatos();
            } else {
                Swal.fire('Error', 'No se pudo registrar (quizás ya existe)', 'error');
            }
        });

    } else { // ACTUALIZAR
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
                    title: 'Actualizado',
                    text: 'Usuario modificado correctamente',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioUsuario.reset();
                $('#md_registrar_usuario').modal('hide');
                $('.modal-backdrop').remove();
                cargarDatos();
            } else {
                Swal.fire('Error', 'No se pudo actualizar', 'error');
            }
        });
    }
});

// ABRIR MODAL EDITAR
document.addEventListener("click", (e) => {
    // Delegación de eventos para elementos dinámicos
    if (e.target.closest(".btn_editar")) {
        const btn = e.target.closest(".btn_editar");
        const id = btn.getAttribute("data-id");
        
        $("#divIdUsuario").show(); // Mostrar campo ID
        $('#formulario_usuario').parsley().reset();
        
        title.innerHTML = '<i class="bi bi-pencil-square"></i> Editar Usuario';
        
        var datos = { "opcion": "editar_consultar", "idUsuario": id };
        
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "usuarioServlt",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                document.querySelector("#idUsuario").value = json[0].usuario.idUsuario;
                document.querySelector("#Usuario").value = json[0].usuario.Usuario;
                document.querySelector("#contrasenia").value = json[0].usuario.contrasenia;
                
                // Setear Radio Button
                const estadoVal = json[0].usuario.estado === "activo" ? "activo" : "inactivo";
                $(`input[name="estado"][value="${estadoVal}"]`).prop("checked", true);

                // Setear Select2
                $('#nombreRol').val(json[0].usuario.nombreRol).trigger('change');
                
                $("#opcion").val("actualizar");
                
                // Abrir Modal BS5
                const myModal = new bootstrap.Modal(document.getElementById('md_registrar_usuario'));
                myModal.show();
                
            } else {
                Swal.fire("Error", "No se encontraron datos", "error");
            }
        });
    }
});

// ELIMINAR
$(document).on("click", ".btn_eliminar", function (e) {
    e.preventDefault();
    const id = $(this).data('id');
    
    Swal.fire({
        title: '¿Estás seguro?',
        text: "El usuario pasará a estado inactivo.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            eliminar(id);
        }
    });
});

function eliminar(id) {
    var datos = { "opcion": "eliminar", "idUsuario": id };
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "usuarioServlt",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            Swal.fire('Eliminado', 'El usuario ha sido desactivado.', 'success');
            cargarDatos();
        } else {
            Swal.fire('Error', 'No se pudo eliminar', 'error');
        }
    });
}

// MOSTRAR INACTIVOS (Toggle)
$(document).ready(function () {
    $("#mostrar_inactivas").on("click", function () {
        const contenedor = $("#contenedor_inactivas");
        contenedor.slideToggle(); // Animación suave

        if (contenedor.is(":visible") || contenedor.css('display') !== 'none') {
            $.ajax({
                method: "POST",
                url: "usuarioServlt",
                data: { opcion: "listar_inactivas" },
                dataType: "json"
            }).done(function (data) {
                const lista = $("#lista_usuarios_inactivas");
                lista.empty();

                if (data.length === 0) {
                    lista.append('<div class="text-center text-muted py-3"><i class="bi bi-emoji-smile"></i> No hay usuarios inactivos.</div>');
                } else {
                    data.forEach(user => {
                        // Renderizado HTML limpio con clases Bootstrap
                        lista.append(`
                            <div class="d-flex justify-content-between align-items-center bg-white p-3 mb-2 rounded shadow-sm border-start border-4 border-danger">
                                <div>
                                    <div class="fw-bold text-dark">${user.Usuario}</div>
                                    <div class="small text-muted text-truncate" style="max-width: 150px;">Clave: ${user.contrasenia}</div>
                                </div>
                                <button class="btn btn-outline-success btn-sm reactivar-usuario px-3" data-id="${user.idUsuario}">
                                    <i class="bi bi-arrow-counterclockwise"></i> Reactivar
                                </button>
                            </div>
                        `);
                    });
                }
            });
        }
    });
});

// REACTIVAR
$(document).on("click", ".reactivar-usuario", function (e) {
    e.preventDefault();
    const idUsuario = $(this).data("id");

    Swal.fire({
        title: "¿Reactivar Cuenta?",
        text: "El usuario volverá a tener acceso al sistema.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        confirmButtonText: "Sí, reactivar",
        cancelButtonText: "Cancelar",
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: "usuarioServlt",
                method: "POST",
                dataType: "json",
                data: {
                    opcion: "reactivar",
                    idUsuario: idUsuario
                },
                success: function (response) {
                    // El response viene como array según tu servlet [json]
                    if (response[0] && response[0].resultado === "exito") {
                        Swal.fire("Reactivado", "Usuario activo nuevamente", "success");
                        cargarDatos(); // Recargar tabla principal
                        $("#mostrar_inactivas").click(); // Cerrar o refrescar lista inactivos
                    } else {
                        Swal.fire("Error", "No se pudo reactivar", "error");
                    }
                }
            });
        }
    });
});




