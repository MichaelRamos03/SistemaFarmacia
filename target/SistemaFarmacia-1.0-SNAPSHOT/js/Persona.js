const btnAddPersona = document.querySelector("#registrar_Persona");
const formularioPersona = document.querySelector("#formulario_Persona");
const combo = document.querySelector("#idUsuario");
const title = document.querySelector("#exampleModalLabel");

document.addEventListener("DOMContentLoaded", () => {
    cargarCombos();
    cargarDatos();

    // Select2 con tema Bootstrap 5
    $('#idUsuario').select2({
        dropdownParent: $('#md_registrar_Persona'),
        theme: 'bootstrap-5',
        width: '100%',
        placeholder: 'Seleccione un usuario'
    });

    $('#formulario_Persona').parsley();
});

// ABRIR MODAL
btnAddPersona.addEventListener('click', () => {
    formularioPersona.reset();
    $('#formulario_Persona').parsley().reset();
    $('#idUsuario').val("").trigger('change');
    
    title.innerHTML = '<i class="bi bi-person-plus-fill"></i> Registro Nuevo Empleado';
    
    const myModal = new bootstrap.Modal(document.getElementById('md_registrar_Persona'));
    myModal.show();
    
    $("#divIdPersona").hide();
});

const cargarCombos = () => {
    var datos = {"opcion": "cargarCombos"};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "PersonaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            combo.innerHTML = '<option value="" disabled selected>Seleccione...</option>' + json[0].usuario;
        } else {
            console.log("Error cargando usuarios");
        }
    }).fail(function () {
        console.log("Fallo ajax combos");
    });
};

const cargarDatos = () => {
    // Spinner mientras carga
    $("#tablita").html(`
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Cargando...</span>
            </div>
            <p class="mt-2 text-muted">Cargando datos...</p>
        </div>
    `);

    const datos = {"opcion": "consultar"};
    
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "PersonaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#tablita").empty().html(json[0].tabla);

            // --- CORRECCIÓN IMPORTANTE: IDIOMA INCRUSTADO (NO URL) ---
            $("#tabla_persona").DataTable({
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

            if(document.querySelector("#personas_registradas")){
                document.querySelector("#personas_registradas").textContent = json[0].cantidad;
            }
        } else {
            Swal.fire("Error", "No se pudo cargar la tabla", "error");
        }
    }).fail(function (jqXHR, textStatus) {
        console.error("Error AJAX:", textStatus);
        $("#tablita").html('<div class="alert alert-danger">Error de conexión al cargar datos.</div>');
    });
};

// INSERTAR O ACTUALIZAR
$(document).on("submit", "#formulario_Persona", function (e) {
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
            url: "PersonaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                Swal.fire({
                    icon: 'success',
                    title: '¡Registrado!',
                    text: 'Empleado guardado correctamente',
                    showConfirmButton: false,
                    timer: 1500
                });
                formularioPersona.reset();
                $('#md_registrar_Persona').modal('hide');
                $('.modal-backdrop').remove();
                cargarDatos();
            } else {
                // Muestra un mensaje si falla (duplicado)
                Swal.fire('Atención', 'No se logró insertar. Verifique que el DUI o el Usuario no estén repetidos.', 'warning');
            }
        });

    } else { // ACTUALIZAR
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "PersonaServlet",
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
                formularioPersona.reset();
                $('#md_registrar_Persona').modal('hide');
                $('.modal-backdrop').remove();
                cargarDatos();
            } else {
                Swal.fire('Info', 'No se logró actualizar', 'info');
            }
        });
    }
});

// ABRIR EDITAR
document.addEventListener("click", (e) => {
    if (e.target.closest(".btn_editar")) {
        const btn = e.target.closest(".btn_editar");
        const id = btn.getAttribute("data-id");
        
        $("#divIdPersona").show();
        $('#formulario_Persona').parsley().reset();
        
        title.innerHTML = '<i class="bi bi-pencil-square"></i> Editar Empleado';
        
        var datos = {"opcion": "editar_consultar", "id": id};
        
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "PersonaServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                document.querySelector("#idPersona").value = json[0].persona.idPersona;
                document.querySelector("#nombre").value = json[0].persona.nombre;
                
                // Formato fecha correcto
                document.querySelector("#fechaNacimiento").value = json[0].persona.fechaNacimiento;
                
                document.querySelector("#dui").value = json[0].persona.dui;
                document.querySelector("#telefono").value = json[0].persona.telefono;
                
                document.querySelector("#idPersona").readOnly = true;
                
                // Setear Select2
                $('#idUsuario').val(json[0].persona.idUsuario).trigger('change');
                
                $("#opcion").val("si_actualizalo");
                
                const myModal = new bootstrap.Modal(document.getElementById('md_registrar_Persona'));
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
        title: '¿Eliminar registro?',
        text: "Esta acción borrará permanentemente al empleado.",
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
    var datos = {"opcion": "eliminar", "id": id};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "PersonaServlet",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            Swal.fire('Eliminado', 'El registro ha sido borrado.', 'success');
            cargarDatos();
        } else {
            Swal.fire('Error', 'No se pudo eliminar', 'error');
        }
    });
}