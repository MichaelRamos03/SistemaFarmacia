/* * Funciones JS para el Login - Estilo Farmacia
 */

$(document).ready(function () {
    $('#loginForm').submit(function (e) {
        e.preventDefault();

        // 1. Referencias
        var btn = $('#btnIngresar');
        var spinner = $('#spinnerCarga');
        var texto = $('#textoBtn');
        var icono = $('#iconoBtn');

        // 2. ACTIVAR MODO CARGA
        btn.prop('disabled', true);
        spinner.removeClass('d-none');
        texto.text('Validando...');
        icono.addClass('d-none');

        const datos = {
            usuario: $('#nombreUsuario').val(),
            contrasenia: $('#contrasenia').val()
        };

        $.ajax({
            dataType: 'json',
            method: 'POST',
            url: 'LoginServlet',
            data: datos,
            success: function (response) {
                if (response.success) {
                    texto.text('Entrando...');
                    
                    // Pequeña alerta de éxito antes de redirigir
                    const Toast = Swal.mixin({
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 1500,
                        timerProgressBar: true
                    });
                    
                    Toast.fire({
                        icon: 'success',
                        title: '¡Bienvenido ' + (response.nombre || '') + '!'
                    });

                    setTimeout(() => {
                        window.location.href = 'Menu.jsp';
                    }, 1000);
                    
                } else {
                    restaurarBoton();
                    // Alerta bonita en lugar de texto plano
                    Swal.fire({
                        icon: 'error',
                        title: 'Acceso Denegado',
                        text: 'Usuario o contraseña incorrectos',
                        confirmButtonColor: '#5a2ca0' // Color morado
                    });
                }
            },
            error: function () {
                restaurarBoton();
                Swal.fire({
                    icon: 'warning',
                    title: 'Error de Conexión',
                    text: 'No se pudo conectar con el servidor',
                    confirmButtonColor: '#ea553d' // Color naranja error
                });
            }
        });

        // Función auxiliar para restaurar el botón si algo sale mal
        function restaurarBoton() {
            btn.prop('disabled', false);
            spinner.addClass('d-none');
            texto.text('INGRESAR');     
            icono.removeClass('d-none');
        }
    });
});