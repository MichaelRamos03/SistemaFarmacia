const btnCategoria = document.querySelector("#registrar_Categoria");
const formularioCategoria = document.querySelector("#formulario_Categoria");
const title = document.querySelector("#exampleModalLabel");

document.addEventListener("DOMContentLoaded", () => {
    cargarDatos();
    $('#formulario_Categoria').parsley();
});

// ABRIR MODAL
btnCategoria.addEventListener('click', () => {
    formularioCategoria.reset();
    $('#formulario_Categoria').parsley().reset();
    
    title.innerHTML = '<i class="bi bi-tag-fill me-2"></i> Registro Nueva Categoría';
    
    const myModal = new bootstrap.Modal(document.getElementById('md_registrar_categoria'));
    myModal.show();
    
    $("#divIdCategoria").hide(); 
    $("#opcion").val("insertar");
    document.querySelector("#estado_activo").checked = true;
});

const cargarDatos = () => {
    // Spinner
    $("#tablita").html(`
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status"></div>
            <p class="mt-2 text-muted">Cargando categorías...</p>
        </div>
    `);

    const datos = {"opcion": "consultar"};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "categoriaServelet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#tablita").empty().html(json[0].tabla);

            // DataTables Configuración
            $("#tabla_categoria").DataTable({
                responsive: true,
                language: {
                    "decimal": "",
                    "emptyTable": "No hay información",
                    "info": "Mostrando _START_ a _END_ de _TOTAL_ Entradas",
                    "infoEmpty": "Mostrando 0 to 0 of 0 Entradas",
                    "infoFiltered": "(Filtrado de _MAX_ total entradas)",
                    "infoPostFix": "",
                    "thousands": ",",
                    "lengthMenu": "Mostrar _MENU_ Entradas",
                    "loadingRecords": "Cargando...",
                    "processing": "Procesando...",
                    "search": "Buscar:",
                    "zeroRecords": "Sin resultados encontrados",
                    "paginate": {
                        "first": "Primero",
                        "last": "Ultimo",
                        "next": "Siguiente",
                        "previous": "Anterior"
                    }
                },
                dom: '<"d-flex justify-content-between align-items-center mb-3"lf>rt<"d-flex justify-content-between align-items-center mt-3"ip>'
            });
            
            if(document.querySelector("#categoria_registradas")){
                document.querySelector("#categoria_registradas").textContent = json[0].cantidad;
            }
        } else {
            Swal.fire("Error", "No se pudo cargar la tabla", "error");
        }
    }).fail(function () {
        console.log("Error al cargar datos");
    });
};

// INSERTAR O ACTUALIZAR
$(document).on("submit", "#formulario_Categoria", function (e) {
    e.preventDefault();
    
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
            url: "categoriaServelet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                Swal.fire({
                    icon: 'success',
                    title: '¡Registrado!',
                    text: 'Categoría guardada correctamente',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioCategoria.reset();
                $('#md_registrar_categoria').modal('hide');
                $('.modal-backdrop').remove();
                cargarDatos();
            } else {
                Swal.fire('Atención', 'No se logró insertar (posible duplicado)', 'warning');
            }
        });

    } else { // ACTUALIZAR
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
                    title: '¡Actualizado!',
                    text: 'Categoría modificada correctamente',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioCategoria.reset();
                $('#md_registrar_categoria').modal('hide');
                $('.modal-backdrop').remove();
                cargarDatos();
            } else {
                Swal.fire('Error', 'No se logró actualizar', 'error');
            }
        });
    }
});

// ABRIR EDITAR (CORREGIDO: DELEGACIÓN DE EVENTOS)
document.addEventListener("click", (e) => {
    // Usamos .closest() para que funcione al dar clic en el ícono
    if (e.target.closest(".btn_editar")) {
        const btn = e.target.closest(".btn_editar");
        const id = btn.getAttribute("data-id");
        
        $("#divIdCategoria").show();
        $('#formulario_Categoria').parsley().reset();

        title.innerHTML = '<i class="bi bi-pencil-square"></i> Editar Categoría';
        
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
                
                // Setear radio buttons
                if(json[0].categoria.estado === "activo"){
                    document.querySelector("#estado_activo").checked = true;
                } else {
                    document.querySelector("#estado_inactivo").checked = true;
                }

                document.querySelector("#idCategoria").readOnly = true;
                
                const myModal = new bootstrap.Modal(document.getElementById('md_registrar_categoria'));
                myModal.show();
                
                $("#opcion").val("si_actualizalo");
            } else {
                Swal.fire("Error", "No se encontraron datos", "error");
            }
        });
    }
});

// ELIMINAR
$(document).on("click", ".btn_eliminar", function (e) {
    e.preventDefault();
    const id = $(this).closest('button').getAttribute('data-id');
    
    Swal.fire({
        title: '¿Eliminar Categoría?',
        text: 'Pasará a la lista de inactivas',
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
    var datos = {"opcion": "eliminar", "idCategoria": id};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "categoriaServelet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            Swal.fire('Eliminado', 'La categoría ha sido desactivada.', 'success');
            cargarDatos();
        } else {
            Swal.fire('Error', 'No se pudo eliminar', 'error');
        }
    });
}

// LISTAR INACTIVAS (AQUÍ ESTABA EL ERROR DEL BOTÓN VERDE)
$(document).ready(function () {
    $("#mostrar_inactivas").on("click", function () {
        const contenedor = $("#contenedor_inactivas");
        contenedor.slideToggle();

        if (contenedor.is(":visible") || contenedor.css('display') !== 'none') {
            const lista = $("#lista_categorias_inactivas");
            lista.html('<p class="text-center text-muted">Cargando...</p>');
            
            $.ajax({
                method: "POST",
                url: "categoriaServelet",
                data: { opcion: "listar_inactivas" }, 
                dataType: "json"
            }).done(function (data) {
                lista.empty();
                if (data.length === 0) {
                    lista.append('<div class="alert alert-light text-center mb-0">No hay categorías inactivas.</div>');
                } else {
                    data.forEach(cat => {
                        // DISEÑO CORREGIDO IDÉNTICO AL USUARIO
                        lista.append(`
                            <div class="d-flex justify-content-between align-items-center bg-white p-3 mb-2 rounded shadow-sm border-start border-4 border-danger">
                                <div>
                                    <div class="fw-bold text-dark">${cat.nombre}</div>
                                    <div class="small text-muted">${cat.descripcion}</div>
                                </div>
                                <button class="btn btn-outline-success btn-sm reactivar-categoria px-3" data-id="${cat.idCategoria}">
                                    <i class="bi bi-arrow-counterclockwise"></i> Reactivar
                                </button>
                            </div>
                        `);
                    });
                }
            }).fail(() => {
                lista.html("<p class='text-danger'>Error al cargar las categorías inactivas.</p>");
            });
        }
    });
});

// REACTIVAR
$(document).on("click", ".reactivar-categoria", function (e) {
    e.preventDefault(); 
    // Usamos closest para asegurar que capturamos el data-id aunque se de click en el icono
    const idCategoria = $(this).closest('button').attr('data-id');

    Swal.fire({
        title: "¿Reactivar categoría?",
        text: "La categoría volverá a estar disponible.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: '#28a745',
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
                dataType: "json",
                success: function (response) {
                    // Verificamos si viene como array o objeto directo
                    let resultado = response[0] ? response[0].resultado : response.resultado;
                    
                    if (resultado === "exito") {
                        Swal.fire({
                            icon: "success",
                            title: "Reactivado",
                            text: "Categoría recuperada exitosamente",
                            showConfirmButton: false,
                            timer: 1500
                        });
                        cargarDatos(); 
                        $("#mostrar_inactivas").click(); 
                        setTimeout(() => { $("#mostrar_inactivas").click(); }, 500); 
                    } else {
                        Swal.fire("Error", "No se pudo reactivar", "error");
                    }
                },
                error: function () {
                    Swal.fire("Error", "Fallo de conexión", "error");
                }
            });
        }
    });
});