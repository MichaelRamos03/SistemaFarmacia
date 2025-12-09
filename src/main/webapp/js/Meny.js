document.addEventListener("DOMContentLoaded", function () {
    const menuToggle = document.getElementById("menu-toggle");
    const sidebar = document.getElementById("sidebar");
    const content = document.querySelector(".content");

    // Variable de estado para saber si está abierto o cerrado
    let menuAbierto = false;

    menuToggle.addEventListener("click", function () {
        if (!menuAbierto) {
            // ABRIR MENÚ
            sidebar.style.left = "0px";
            // Empujamos el contenido 250px a la derecha
            content.style.marginLeft = "250px";
            // Movemos el botón junto con el menú (250px + margen)
            menuToggle.style.left = "260px"; 
            
            menuAbierto = true;
        } else {
            // CERRAR MENÚ
            sidebar.style.left = "-250px";
            content.style.marginLeft = "0";
            // Regresamos el botón a su posición original
            menuToggle.style.left = "15px"; 
            
            menuAbierto = false;
        }
    });
});