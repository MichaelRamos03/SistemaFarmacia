/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


const btnCategoria = document.querySelector("#registrar_Categoria");
const formularioCategoria = document.querySelector("#formulario_Categoria");
const title = document.querySelector("#exampleModalLabel");
const error = document.querySelector("#error");

document.addEventListener("DOMContentLoaded", () => {
    cargarDatos();
    $('#formulario_Categoria').parsley();
});

btnCategoria.addEventListener('click', () => {
    formularioCategoria.reset();
    $('#formulario_Categoria').parsley().reset();
    title.innerHTML = "<h5 class='modal-title'\n\
 id='exampleModalLabel'>Registro nueva Categoria\n\
<br><sub> Todos los campos son obligatorios</sub>";
    $("#md_registrar_categoria").modal("show");
    console.log("entró a cargar modal ");
    $("#idCategoria").hide(); 
    $("#labelId").hide(); 
});

const cargarDatos = () => {
    mostrar_cargando("Procesando Solicitud",
            "Espere un momento mientras se obtiene la información solicitada");
    const datos = {"opcion": "consultar"};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "categoriaServelet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#tablita").empty().html(json[0].tabla);

            $("#tabla_categoria").DataTable({
                "language": {
                    "url": "https://cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"


                }
            });
            document.querySelector("#categoria_registradas").textContent
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
formularioCategoria.addEventListener("submit", (e) => {
    e.preventDefault();

    const datos = $("#formulario_Categoria").serialize();
    console.log("DATOS A INSERTAR/MODIFICAR " + datos);
    console.log("document.querySelector opcion.value"
            + document.querySelector("#opcion").value);
    if (document.querySelector("#opcion").value === "insertar") {          
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "categoriaServelet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                Swal.fire({
                    icon: 'success',
                    title: 'Categoria Registrada',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioCategoria.reset();
                $("#md_registrar_categoria").modal("hide");
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
            url: "categoriaServelet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#opcion").val("insertar");
                Swal.fire({
                    icon: 'success',
                    title: 'Categoria Actualizada',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioCategoria.reset();
                $("#md_registrar_categoria").modal("hide");
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
        $("#idCategoria").show();
        $("#labelId").show();
        $('#formulario_Categoria').parsley().reset();

        title.innerHTML = "<h5 class='modal-title' id='exampleModalLabel'>Editar Categoria<br><sub> Todos los campos son obligatorios</sub>";
        const id = e.target.getAttribute("data-id");
        var datos = { "opcion": "editar_consultar", "idCategoria": id };

        $.ajax({
            dataType: "json",
            method: "POST",
            url: "categoriaServelet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                
                $("#contenedor_inactivas").hide();
                $("#lista_categorias_inactivas").empty();

                document.querySelector("#idCategoria").value = json[0].categoria.idCategoria;
                document.querySelector("#nombreCategoria").value = json[0].categoria.nombreCategoria;
                document.querySelector("#descripcion").value = json[0].categoria.descripcion;
                document.querySelector(`input[name="estado"][value="${json[0].categoria.estado}"]`).checked = true;

                document.querySelector("#idCategoria").readOnly = true;
                $("#md_registrar_categoria").modal("show");
                $("#opcion").val("si_actualizalo");
            } else {
                Swal.fire(
                    "Error",
                    "No se pudo completar la petición, intentelo más tarde",
                    "error"
                );
            }
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
    var datos = {"opcion": "eliminar", "idCategoria": id};
    console.log("id a eliminar es: " + id);
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "categoriaServelet",
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
                url: "categoriaServelet",
                data: { opcion: "listar_inactivas" }, 
                dataType: "json"
            }).done(function (data) {
                const lista = $("#lista_categorias_inactivas");
                lista.empty();

                if (data.length === 0) {
                    lista.append("<p>No hay categorías inactivas.</p>");
                } else {
                    data.forEach(cat => {
                        lista.append(`
                            <div class="d-flex justify-content-between align-items-center border-bottom py-2">
                                <div>
                                    <strong>${cat.nombre}</strong><br>
                                    <small>${cat.descripcion}</small>
                                </div>
                                <button class="btn btn-su${cat.nombre}</strong><br>
                                    <small>${cat.descripcion}</ccess btn-sm reactivar-categoria" data-id="${cat.idCategoria}">
                                    Reactivar
                                </button>
                            </div>
                        `);
                    });
                }
            }).fail(() => {
                $("#lista_categorias_inactivas").html("<p>Error al cargar las categorías inactivas.</p>");
            });
        }
    });

   
});

$(document).on("click", ".reactivar-categoria", function (e) {
    e.preventDefault(); 

    const idCategoria = $(this).data("id");

    Swal.fire({
        title: "¿Reactivar categoría?",
        text: "Esta acción reactivará la categoría seleccionada.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, reactivar",
        cancelButtonText: "Cancelar",
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: "categoriaServelet",
                method: "POST",
                data: {
                    opcion: "reactivar",
                    idCategoria: idCategoria
                },
                success: function (response) {
                    if (response[0]?.resultado === "exito") {
                        Swal.fire({
                            icon: "success",
                            title: "Reactivado",
                            text: "La categoría fue reactivada exitosamente",
                            showConfirmButton: false,
                            timer: 1500
                        });

                        
                        setTimeout(() => {
                            location.reload(); 
                        }, 1600);

                    } else {
                        Swal.fire("Error", "No se pudo reactivar la categoría", "error");
                    }
                },
                error: function () {
                    Swal.fire("Error", "Error en la petición AJAX", "error");
                }
            });
        }
    });
});


