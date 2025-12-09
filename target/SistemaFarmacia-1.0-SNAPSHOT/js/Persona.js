/* JS GESTIÓN DE PERSONAL - Corregido y Estilizado */

const formularioPersona = document.querySelector("#formulario_Persona");
const title = document.querySelector("#exampleModalLabel");

document.addEventListener("DOMContentLoaded", () => {
    cargarCombos();
    cargarDatos();

    // Select2 BS5 Theme
    $('#idUsuario').select2({
        dropdownParent: $('#md_registrar_Persona'),
        theme: 'bootstrap-5',
        width: '100%',
        placeholder: 'Seleccione un usuario'
    });

    $('#formulario_Persona').parsley();
});

// ABRIR MODAL (Asignación de evento segura con jQuery)
$("#registrar_Persona").on('click', function() {
    formularioPersona.reset();
    $('#formulario_Persona').parsley().reset();
    $('#idUsuario').val("").trigger('change');
    
    // Cambiar Título con Icono
    $("#exampleModalLabel").html('<i class="bi bi-person-plus-fill"></i> Registro Nuevo Empleado');
    
    $("#md_registrar_Persona").modal("show");
    $("#divIdPersona").hide();
    $("#opcion").val("insertar");
});

const cargarCombos = () => {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "PersonaServlet",
        data: {"opcion": "cargarCombos"}
    }).done(function (json) {
        // Validación segura
        var data = Array.isArray(json) ? json[0] : json;
        
        if (data.resultado === "exito") {
            $("#idUsuario").html('<option value="" disabled selected>Seleccione...</option>' + data.usuario);
        }
    });
};

const cargarDatos = () => {
    const datos = {"opcion": "consultar"};
    
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "PersonaServlet",
        data: datos
    }).done(function (json) {
        var data = Array.isArray(json) ? json[0] : json;
        
        if (data.resultado === "exito") {
            $("#tablita").empty().html(data.tabla);

            // DataTable con destroy:true para evitar errores
            $("#tabla_persona").DataTable({
                "language": { "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json" },
                "responsive": true,
                "autoWidth": false,
                "destroy": true 
            });

            if(document.querySelector("#personas_registradas")){
                document.querySelector("#personas_registradas").textContent = data.cantidad;
            }
        } else {
            Swal.fire("Error", "No se pudo cargar la tabla", "error");
        }
    }).fail(function () {
        console.log("Error de conexión al cargar datos");
    });
};

// INSERTAR O ACTUALIZAR
$("#formulario_Persona").on("submit", function (e) {
    e.preventDefault();
    if (!$(this).parsley().isValid()) return;
    
    const datos = $(this).serialize();
    const opcion = $("#opcion").val();

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "PersonaServlet",
        data: datos
    }).done(function (json) {
        var data = Array.isArray(json) ? json[0] : json;
        
        if (data.resultado === "exito") {
            Swal.fire({
                icon: 'success',
                title: (opcion === "insertar") ? 'Registrado' : 'Actualizado',
                showConfirmButton: false,
                timer: 1500
            });
            $("#md_registrar_Persona").modal('hide');
            cargarDatos();
        } else {
            Swal.fire('Error', 'Operación fallida. Verifique duplicados.', 'warning');
        }
    });
});

// ABRIR EDITAR (Delegación de eventos)
$(document).on("click", ".btn_editar", function () {
    const id = $(this).data("id");
    
    $("#divIdPersona").show();
    $('#formulario_Persona').parsley().reset();
    $("#exampleModalLabel").html('<i class="bi bi-pencil-square"></i> Editar Empleado');
    $("#opcion").val("si_actualizalo");

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "PersonaServlet",
        data: {"opcion": "editar_consultar", "id": id}
    }).done(function (json) {
        var data = Array.isArray(json) ? json[0] : json;
        
        if (data.resultado === "exito") {
            $("#idPersona").val(data.persona.idPersona);
            $("#nombre").val(data.persona.nombre);
            $("#fechaNacimiento").val(data.persona.fechaNacimiento);
            $("#dui").val(data.persona.dui);
            $("#telefono").val(data.persona.telefono);
            
            // Cargar Select2
            $('#idUsuario').val(data.persona.idUsuario).trigger('change');
            
            $("#md_registrar_Persona").modal("show");
        }
    });
});

// ELIMINAR
$(document).on("click", ".btn_eliminar", function () {
    const id = $(this).data('id'); 
    
    Swal.fire({
        title: '¿Eliminar registro?',
        text: "Esta acción no se puede deshacer.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                dataType: "json",
                method: "POST",
                url: "PersonaServlet",
                data: {"opcion": "eliminar", "id": id}
            }).done(function (json) {
                var data = Array.isArray(json) ? json[0] : json;
                if (data.resultado === "exito") {
                    Swal.fire('Eliminado', 'Registro borrado.', 'success');
                    cargarDatos();
                } else {
                    Swal.fire('Error', 'No se pudo eliminar', 'error');
                }
            });
        }
    });
});