/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

$(document).ready(function () {
    $('#loginForm').submit(function (e) {
        e.preventDefault();

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
                    window.location.href = 'Menu.jsp';
                } else {
                    $('#mensaje').text('Usuario o contraseña incorrectos.');
                }
            },
            error: function () {
                $('#mensaje').text('Error en la conexión con el servidor.');
            }
        });
    });
});
