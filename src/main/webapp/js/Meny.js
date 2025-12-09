

document.addEventListener("DOMContentLoaded", function () {
    const menuToggle = document.getElementById("menu-toggle");
    const sidebar = document.getElementById("sidebar");
    const content = document.querySelector(".content");

    menuToggle.addEventListener("click", function () {
        if (sidebar.style.left === "0px") {
            sidebar.style.left = "-250px";
            content.style.marginLeft = "0";
            menuToggle.style.left = " -2px";
        } else {
            sidebar.style.left = "0px";
            content.style.marginLeft = "250px";
            menuToggle.style.left = "313px";
        }
    });
});