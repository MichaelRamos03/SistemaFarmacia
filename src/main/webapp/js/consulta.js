/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

$(document).ready(function () {
            const datos = { opcion: "consultar" };

            $.ajax({
                method: "POST",
                url: "consultaServelet",
                data: datos,
                dataType: "json"
            }).done(function (json) {
                if (json[0].resultado === "exito") {
                    $("#contenedor_tabla").html(json[0].tabla);
                $("#tabla").DataTable({
                    language: {
                        url: "https://cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
                    }
                });
                
            } else {
                $("#contenedor_tabla").html("<div class='alert alert-danger'>No se pudo cargar la información.</div>");
            } 
            }).fail(function () {
                $("#contenedor_tabla").html("<div class='alert alert-danger'>Error al consultar existentes.</div>");
            });
        });
        
        
        