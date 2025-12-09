/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controller;

import com.ues.edu.controler.persistencia.RolJpaControler;
import com.ues.edu.controler.persistencia.UsuarioJpaControler;
import com.ues.edu.controler.persistencia.categoriaJpaControler;
import com.ues.edu.entities.Rol;
import com.ues.edu.entities.Usuario;
import com.ues.edu.persistencia.exception.NonexistentEntityException;
import com.ues.edu.utilidades.Encriptar;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Gaby Laínez
 */
@WebServlet(name = "usuarioServlt", urlPatterns = {"/usuarioServlt"})
public class usuarioServlt extends HttpServlet {

    private List<Rol> rolesList;
    private List<Usuario> usuariosList;
    private Rol rol;
    private Usuario usuario;
    private Usuario usuarioRecuperado;

    private JSONArray array = new JSONArray();
    private JSONObject json = new JSONObject();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet usuarioServlt</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet usuarioServlt at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String op = request.getParameter("opcion");
        System.out.println("OPCION EN doPost " + op);
        switch (op) {
            case "cargarCombos": {
                String comboRoles = "";
                RolJpaControler rolJpaControl = new RolJpaControler(); // llamada al emf
                this.rolesList = new ArrayList<>();
                this.rolesList = rolJpaControl.findRolEntities();
                if (!this.rolesList.isEmpty()) {
                    for (Rol roles : this.rolesList) {
                        comboRoles += "<option value=" + roles.getIdRol() + ">"
                                + roles.getNombreRol() + "</option>";
                    }
                    this.json.put("resultado", "exito");
                    this.json.put("roles", comboRoles);
                    this.array.put(this.json);
                } else {
                    this.json.put("resultado", "error");
                }
                response.getWriter().write(this.array.toString());
            }
            break;

            case "insertar": // Cuando también modifica.
            {
                String nombreUsuario = request.getParameter("Usuario");
                String contrasenia = request.getParameter("contrasenia");
                String contraseniaEncriptada = Encriptar.getStringMessageDigest(contrasenia, Encriptar.SHA256);

                int idRol = Integer.parseInt(request.getParameter("nombreRol"));
                String estadoStr = request.getParameter("estado");
                boolean estado = "activo".equalsIgnoreCase(estadoStr);
                this.rol = new Rol(idRol);

                Usuario objUsuario = new Usuario(nombreUsuario, contraseniaEncriptada, estado, this.rol);

                UsuarioJpaControler usuarioJpaControl = new UsuarioJpaControler(); // emf

                System.out.println("nombre usuario " + nombreUsuario);
                System.out.println("contraseña  " + contraseniaEncriptada);
                System.out.println("rol  " + idRol);

// VERIFICANDO QUE EL USUARIO,CONTRASEÑA Y ROL NO EXISTA
                Usuario usuarioValidado = new Usuario();
                usuarioValidado = usuarioJpaControl.validarUsuario(objUsuario);

                if (usuarioValidado == null) {
                    // INSERTAR REGISTROS
                    String insertar = usuarioJpaControl.create(objUsuario);

                    System.out.println(" String insertar " + insertar);

                    if (insertar.equals("exito")) {
                        this.json.put("resultado", "exito");

                    } else {
                        System.out.println("nombre usuario  en usuario != null else " + usuarioValidado.getUsuario());

                        System.out.println("contraseña   en usuario != null else " + usuarioValidado.getContrasenia());
                        System.out.println("rol   en usuario != null else " + usuarioValidado.getRol().getIdRol());

                        this.json.put("resultado", "error");
                    }

                } else {
                    // USUARIO, CONTRASEÑA Y ROL YA EXISTE
                    // NO INSERTAR
                    this.json.put("resultado", "error registro existente");
                }
                this.array.put(this.json);
                response.getWriter().write(array.toString());
            }
            break;

            case "actualizar": {
                try // Cuando modifica.
                {
                    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                    String nombreUsuario = request.getParameter("Usuario");
                    String contrasenia = request.getParameter("contrasenia");
                    int idRol = Integer.parseInt(request.getParameter("nombreRol"));
                    String estadoStr = request.getParameter("estado");
                    boolean estado = "activo".equalsIgnoreCase(estadoStr);

                    this.rol = new Rol(idRol);
                    Usuario objUsuario = new Usuario(idUsuario, nombreUsuario, contrasenia, estado, this.rol);
                    UsuarioJpaControler usuarioJpaControl
                            = new UsuarioJpaControler(); // emf
                    String actualizar = usuarioJpaControl.edit(objUsuario);
                    if (actualizar.equals("exito")) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error");
                    }
                    this.array.put(this.json);
                    response.getWriter().write(this.array.toString());
                } catch (Exception ex) {
                    Logger.getLogger(usuarioServlt.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            break;

            case "consultar": {
                UsuarioJpaControler usuarioJpaControl = new UsuarioJpaControler(); // emf

                // Encabezados de la tabla (Diseño limpio)
                String html = "<table id=\"tabla_usuarios\" class=\"table table-hover table-bordered nowrap\" style=\"width:100%\">\n"
                        + "  <thead class=\"bg-light\">\n"
                        + "    <tr>\n"
                        + "      <th scope=\"col\">#</th>\n"
                        + "      <th scope=\"col\">Usuario</th>\n"
                        + "      <th scope=\"col\">Rol</th>\n"
                        + "      <th scope=\"col\" class=\"text-center\">Estado</th>\n"
                        + "      <th scope=\"col\" class=\"text-center\">Acciones</th>\n"
                        + "    </tr>\n"
                        + "  </thead>\n"
                        + "  <tbody>";

                this.usuariosList = new ArrayList<>();
                this.usuariosList = usuarioJpaControl.findUsuarioActivas();
                int i = 1;

                for (Usuario objUsuario : this.usuariosList) {

                    // Badge de Estado (Pastilla Verde o Roja)
                    String estadoBadge = objUsuario.isEstado()
                            ? "<span class='badge bg-success bg-opacity-10 text-success px-3 rounded-pill'>Activo</span>"
                            : "<span class='badge bg-danger bg-opacity-10 text-danger px-3 rounded-pill'>Inactivo</span>";

                    // Inicial del nombre para el círculo de color
                    String inicial = (objUsuario.getUsuario() != null && !objUsuario.getUsuario().isEmpty())
                            ? objUsuario.getUsuario().substring(0, 1).toUpperCase()
                            : "?";

                    html += "  <tr>\n"
                            + "      <td class='align-middle fw-bold'>" + i + "</td>\n"
                            + "      <td class='align-middle'>"
                            + "         <div class='d-flex align-items-center'>"
                            + "            <div class='rounded-circle bg-purple-light text-purple d-flex justify-content-center align-items-center me-2' style='width:35px; height:35px; font-weight:bold;'>"
                            + inicial
                            + "            </div>"
                            + "            <div>" + objUsuario.getUsuario() + "</div>"
                            + "         </div>"
                            + "      </td>\n"
                            + "      <td class='align-middle'>" + objUsuario.getRol().getNombreRol() + "</td>\n"
                            + "      <td class='align-middle text-center'>" + estadoBadge + "</td>\n"
                            + "      <td class='align-middle text-center'>\n";

                    // --- INICIO: BOTONES DE ACCIÓN DIRECTA ---
                    html += "        <div class='d-flex justify-content-center gap-2'>\n";

                    // 1. Botón EDITAR (Morado)
                    html += "          <button class='btn btn-sm btn-outline-purple btn_editar' data-id='" + objUsuario.getId() + "' title='Editar usuario'>\n"
                            + "            <i class='bi bi-pencil-fill'></i>\n"
                            + "          </button>\n";

                    // 2. Botón ELIMINAR / REACTIVAR
                    if (objUsuario.isEstado()) {
                        // Si está activo -> Botón Rojo (Basurero)
                        html += "          <button class='btn btn-sm btn-outline-danger btn_eliminar' data-id='" + objUsuario.getId() + "' title='Desactivar usuario'>\n"
                                + "            <i class='bi bi-trash-fill'></i>\n"
                                + "          </button>\n";
                    } else {
                        // Si está inactivo -> Botón Verde (Flecha Reactivar)
                        html += "          <button class='btn btn-sm btn-outline-success btn_activar' data-id='" + objUsuario.getId() + "' title='Reactivar usuario'>\n"
                                + "            <i class='bi bi-arrow-counterclockwise'></i>\n"
                                + "          </button>\n";
                    }

                    html += "        </div>\n";
                    // --- FIN BOTONES ---

                    html += "      </td>\n"
                            + "  </tr>";
                    i++;
                }

                html += "  </tbody>\n" + "</table>";

                this.json.put("resultado", "exito");
                this.json.put("tabla", html);
                this.json.put("cantidad", this.usuariosList.size()); // Actualiza el contador de cards
                this.array.put(this.json);
                response.getWriter().write(this.array.toString());
            }
            break;

            case "editar_consultar": {
                JSONObject usuarioJsonObject = new JSONObject();
                UsuarioJpaControler usuarioModel = new UsuarioJpaControler(); //emf
                int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                this.usuario = new Usuario(); // Es indispensable antes de setear sino da 

                this.usuario.setId(idUsuario);

                this.usuarioRecuperado = new Usuario();
                this.usuarioRecuperado = usuarioModel.findUsuario(idUsuario);
                if (this.usuarioRecuperado != null) {
                    usuarioJsonObject.put("idUsuario", this.usuarioRecuperado.getId());
                    usuarioJsonObject.put("Usuario", this.usuarioRecuperado.getUsuario());
                    usuarioJsonObject.put("nombreRol", this.usuarioRecuperado.getRol().getIdRol());
                    usuarioJsonObject.put("contrasenia", this.usuarioRecuperado.getContrasenia());
                    usuarioJsonObject.put("estado", this.usuarioRecuperado.isEstado() ? "activo" : "inactivo");

                    this.json.put("resultado", "exito");
                    this.json.put("usuario", usuarioJsonObject);
                } else {
                    this.json.put("resultado", "error");
                }
                this.array.put(this.json);
                response.getWriter().write(this.array.toString());
            }
            break;

            case "eliminar": {
                try {
                    String resultado = "";
                    UsuarioJpaControler UsuarioModel = null;
                    UsuarioModel = new UsuarioJpaControler(); //emf
                    int idElim = Integer.parseInt(request.getParameter("idUsuario"));
                    resultado = UsuarioModel.destroy(idElim);
                    if ("exito".equals(resultado)) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error_eliminar");
                    }
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(categoriaServelet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.array.put(this.json);
            response.getWriter().write(this.array.toString());
            break;

            case "listar_inactivas": {
                UsuarioJpaControler UsuarioModel = new UsuarioJpaControler();
                List<Usuario> lista = UsuarioModel.findUsuarioInactivas();

                JSONArray listaJson = new JSONArray();
                for (Usuario c : lista) {
                    JSONObject obj = new JSONObject();
                    obj.put("idUsuario", c.getId());
                    obj.put("Usuario", c.getUsuario());
                    obj.put("contrasenia", c.getContrasenia());
                    listaJson.put(obj);
                }

                response.setContentType("application/json");
                response.getWriter().write(listaJson.toString());

            }
            break;
            case "reactivar": {
                UsuarioJpaControler usuarioControl = new UsuarioJpaControler();
                int id = Integer.parseInt(request.getParameter("idUsuario"));

                String resultado = usuarioControl.reactivar(id);
                json.put("resultado", resultado.equals("exito") ? "exito" : "error");

                array.put(json);
                response.getWriter().write(array.toString());
                break;
            }

        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
