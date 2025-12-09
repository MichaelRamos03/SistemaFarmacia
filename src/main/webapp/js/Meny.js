/* JS MENÚ LATERAL */

document.addEventListener("DOMContentLoaded", function () {
    const menuToggle = document.getElementById("menu-toggle");
    const sidebar = document.getElementById("sidebar");
    const content = document.querySelector(".content");

    // Estado del menú
    let menuAbierto = false;

    // Ancho del sidebar (Debe coincidir con el CSS)
    const sidebarWidth = "260px";

    menuToggle.addEventListener("click", function () {
        if (!menuAbierto) {
            // --- ABRIR ---
            sidebar.style.left = "0px";
            
            // Empujar contenido (opcional, si quieres que se encoja)
            // content.style.marginLeft = sidebarWidth; 
            
            // Mover botón hacia la derecha para que siga visible junto al menú
            menuToggle.style.left = `calc(${sidebarWidth} + 15px)`;
            
            // Cambiar ícono a "X"
            menuToggle.innerHTML = '<i class="bi bi-x-lg"></i>';
            
            menuAbierto = true;
        } else {
            // --- CERRAR ---
            sidebar.style.left = `-${sidebarWidth}`;
            content.style.marginLeft = "0";
            
            // Regresar botón a la izquierda
            menuToggle.style.left = "20px";
            
            // Cambiar ícono a Hamburguesa
            menuToggle.innerHTML = '<i class="bi bi-list"></i>';
            
            menuAbierto = false;
        }
    });
    
    // CERRAR SI SE DA CLICK FUERA (Opcional, mejora UX)
    content.addEventListener('click', function() {
        if(menuAbierto) {
            menuToggle.click();
        }
    });
});