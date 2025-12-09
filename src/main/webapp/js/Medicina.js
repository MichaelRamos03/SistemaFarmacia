const btnAddMedicina = document.querySelector("#registrar_medicina");
const formularioMedicina = document.querySelector("#formulario_medicina");
const combo = document.querySelector("#idCategoria");
const title = document.querySelector("#exampleModalLabel");

document.addEventListener("DOMContentLoaded", () => {
    cargarCombos();
    cargarDatos();

    $('#idCategoria').select2({
        dropdownParent: $('#md_registrar_medicina'),
        theme: 'bootstrap-5',
        width: '100%',
        placeholder: 'Seleccione una categoría'
    });

    $('#formulario_medicina').parsley();
});

// ABRIR MODAL
btnAddMedicina.addEventListener('click', () => {
    formularioMedicina.reset();
    $('#formulario_medicina').parsley().reset();
    $('#idCategoria').val("").trigger('change');
    
    title.innerHTML = '<i class="bi bi-capsule-pill"></i> Registro Nuevo Medicamento';
    
    const myModal = new bootstrap.Modal(document.getElementById('md_registrar_medicina'));
    myModal.show();
    
    $("#divIdMedicamento").hide();
    $("#opcion").val("insertar");
    document.querySelector("#estado_activo").checked = true;
});

const cargarCombos = () => {
    var datos = {"opcion": "cargarCombos"};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "medicinaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            combo.innerHTML = '<option value="" disabled selected>Seleccione...</option>' + json[0].categoria;
        } else {
            console.log("Error cargando categorías");
        }
    }).fail(function () {
        console.log("Fallo ajax combos");
    });
};

const cargarDatos = () => {
    $("#tablita").html(`
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status"></div>
            <p class="mt-2 text-muted">Cargando inventario...</p>
        </div>
    `);

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

            if(document.querySelector("#medicina_registradas")){
                document.querySelector("#medicina_registradas").textContent = json[0].cantidad;
            }
        } else {
            Swal.fire("Error", "No se pudo cargar la tabla", "error");
        }
    }).fail(function () {
        console.log("Error ajax consultar");
    });
};

// INSERTAR O ACTUALIZAR
$(document).on("submit", "#formulario_medicina", function (e) {
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
            url: "medicinaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                Swal.fire({
                    icon: 'success',
                    title: '¡Registrado!',
                    text: 'Medicamento guardado correctamente',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioMedicina.reset();
                $('#md_registrar_medicina').modal('hide');
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
            url: "medicinaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#opcion").val("insertar");
                Swal.fire({
                    icon: 'success',
                    title: 'Actualizado',
                    text: 'Datos modificados correctamente',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioMedicina.reset();
                $('#md_registrar_medicina').modal('hide');
                $('.modal-backdrop').remove();
                cargarDatos();
            } else {
                Swal.fire('Info', 'No se logró actualizar', 'info');
            }
        });
    }
});

// =======================================================
//  CORRECCIÓN 1: EDICIÓN (Detectar clic en icono)
// =======================================================
document.addEventListener("click", (e) => {
    // Usamos .closest() para buscar el botón padre si se hace clic en el icono
    const btn = e.target.closest(".btn_editar");
    
    if (btn) {
        const id = btn.getAttribute("data-id"); // Ahora siempre obtendrá el ID
        
        $("#divIdMedicamento").show();
        $('#formulario_medicina').parsley().reset();
        
        title.innerHTML = '<i class="bi bi-pencil-square"></i> Editar Medicamento';
        
        var datos = { "opcion": "editar_consultar", "idMedicamento": id };
        
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
                
                if(json[0].medicina.activo === "activo"){
                    document.querySelector("#estado_activo").checked = true;
                } else {
                    document.querySelector("#estado_inactivo").checked = true;
                }
                
                document.querySelector("#idMedicamento").readOnly = true;
                $('#idCategoria').val(json[0].medicina.idCategoria).trigger('change');
                
                $("#opcion").val("si_actualizalo");
                
                const myModal = new bootstrap.Modal(document.getElementById('md_registrar_medicina'));
                myModal.show();
            } else {
                Swal.fire("Error", "No se encontraron datos", "error");
            }
        });
    }
});

// =======================================================
//  CORRECCIÓN 2: ELIMINAR (Detectar clic en icono)
// =======================================================
$(document).on("click", ".btn_eliminar", function (e) {
    e.preventDefault();
    // Usamos $(this).closest para asegurar que agarramos el botón
    const id = $(this).closest('button').attr('data-id');
    
    Swal.fire({
        title: '¿Eliminar registro?',
        text: "El medicamento cambiará a estado inactivo.",
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
    var datos = { "opcion": "eliminar", "idMedicamento": id };
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "medicinaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            Swal.fire('Eliminado', 'Registro actualizado.', 'success');
            cargarDatos();
        } else {
            Swal.fire('Error', 'No se pudo eliminar', 'error');
        }
    });
}

// LOGICA PAPELERA Y REACTIVAR
$(document).ready(function () {
    $("#mostrar_inactivas").on("click", function () {
        const contenedor = $("#contenedor_inactivas");
        contenedor.slideToggle();

        if (contenedor.is(":visible") || contenedor.css('display') !== 'none') {
            const lista = $("#lista_medicamentos_inactivos");
            lista.html('<p class="text-center text-muted">Cargando...</p>');
            
            $.ajax({
                method: "POST",
                url: "medicinaServlet",
                data: { opcion: "listar_inactivas" }, 
                dataType: "json"
            }).done(function (data) {
                lista.empty();
                if (data.length === 0) {
                    lista.append('<div class="alert alert-light text-center mb-0">No hay medicamentos inactivos.</div>');
                } else {
                    data.forEach(med => {
                        lista.append(`
                            <div class="d-flex justify-content-between align-items-center bg-white p-3 mb-2 rounded shadow-sm border-start border-4 border-danger">
                                <div>
                                    <strong class="text-dark">${med.nombre}</strong><br>
                                    <small class="text-muted">Stock: ${med.stock}</small>
                                </div>
                                <button class="btn btn-sm btn-outline-success reactivar-medicina px-3" data-id="${med.idMedicamento}">
                                    <i class="bi bi-arrow-counterclockwise"></i> Reactivar
                                </button>
                            </div>
                        `);
                    });
                }
            }).fail(() => {
                lista.html("<p class='text-danger'>Error al cargar inactivos.</p>");
            });
        }
    });
});

$(document).on("click", ".reactivar-medicina", function (e) {
    e.preventDefault(); 
    const idMed = $(this).closest('button').attr('data-id');

    Swal.fire({
        title: "¿Reactivar medicamento?",
        text: "Volverá a estar disponible en el inventario.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        confirmButtonText: "Sí, reactivar",
        cancelButtonText: "Cancelar",
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: "medicinaServlet",
                method: "POST",
                data: {
                    opcion: "reactivar",
                    idMedicamento: idMed
                },
                dataType: "json",
                success: function (response) {
                    let resultado = response[0] ? response[0].resultado : response.resultado;
                    
                    if (resultado === "exito") {
                        Swal.fire("Reactivado", "Medicamento recuperado exitosamente", "success");
                        cargarDatos(); 
                        $("#mostrar_inactivas").click(); 
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