/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

$(document).ready(function () {
    $('#loginForm').submit(function (e) {
        e.preventDefault();

        // 1. Referencias a los elementos visuales
        var btn = $('#btnIngresar');
        var spinner = $('#spinnerCarga');
        var texto = $('#textoBtn');
        var icono = $('#iconoBtn');
        var mensaje = $('#mensaje');

        // 2. ACTIVAR MODO CARGA
        // Desactivamos el botón para evitar doble clic
        btn.prop('disabled', true);
        // Mostramos el spinner
        spinner.removeClass('d-none');
        // Cambiamos el texto
        texto.text('Validando...');
        // Ocultamos el icono de la puerta para que no se vea amontonado
        icono.addClass('d-none');
        // Limpiamos mensajes de error previos
        mensaje.text('');

        const datos = {
            usuario: $('#nombreUsuario').val(),
            contrasenia: $('#contrasenia').val()
        };

        $.ajax({
            dataType: 'json',
            method: 'POST',
            url: 'LoginServlet',
            data: datos,
            // Agregamos un pequeño retraso artificial (opcional) para que se aprecie la animación
            // Si tu servidor es muy rápido, el spinner solo parpadeará.
            success: function (response) {
                if (response.success) {
                    // Si es correcto, cambiamos texto a "Entrando..." y redirigimos
                    texto.text('Entrando...');
                    // No habilitamos el botón porque ya nos vamos de la página
                    window.location.href = 'Menu.jsp';
                } else {
                    // ERROR DE CREDENCIALES
                    mostrarError('Usuario o contraseña incorrectos.');
                }
            },
            error: function () {
                // ERROR DE SERVIDOR
                mostrarError('Error en la conexión con el servidor.');
            }
        });

        // Función auxiliar para restaurar el botón si algo sale mal
        function mostrarError(msg) {
            // Mostramos el mensaje rojo
            mensaje.text(msg);
            
            // Restauramos el botón a su estado original
            btn.prop('disabled', false);
            spinner.addClass('d-none'); // Ocultar spinner
            texto.text('INGRESAR');     // Volver texto original
            icono.removeClass('d-none');// Mostrar icono puerta
        }
    });
});