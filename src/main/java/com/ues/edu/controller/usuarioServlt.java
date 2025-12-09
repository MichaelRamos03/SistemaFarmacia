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
                UsuarioJpaControler usuarioJpaControl = new UsuarioJpaControler(); //emf
                String html = "<table class=\"table\" id=\"tabla_usuarios\""
                        + "class=\"table table-bordered dt-responsive nowrap\" width=\"100%\">\n"
                        + "  <thead>\n"
                        + "    <tr>\n"
                        + "      <th scope=\"col\">#</th>\n"
                        + "      <th scope=\"col\">Nombre Usuario</th>\n"
                        + "      <th scope=\"col\">Contraseña</th>\n"
                        + "      <th scope=\"col\">Rol</th>\n"
                        + "      <th scope=\"col\">estado</th>\n"
                        + "      <th scope=\"col\">Acciones</th>\n"
                        + "    </tr>\n"
                        + "  </thead>\n"
                        + "  <tbody>";
                this.usuariosList = new ArrayList<>();
                this.usuariosList = usuarioJpaControl.findUsuarioActivas();
                int cont = 0;
                int i = 1;

                for (Usuario objUsuario : this.usuariosList) {
                    cont++;
                    html += "  <tr>\n"
                            + "      <td>" + i + "</td>\n"
                            + "      <td>" + objUsuario.getUsuario() + "</td>\n"
                            + "      <td>" + objUsuario.getContrasenia() + "</td>\n"
                            + "      <td>" + objUsuario.getRol().getNombreRol() + "</td>\n"
                            + "      <td>" + (objUsuario.isEstado() ? "Activo" : "Inactivo") + "</td>\n"
                            + "<td>"
                            + "<div class='dropdown m-b-10'>"
                            + "<button class='btn btn-secondary dropdown-toggle'"
                            + " type='button' id='dropdownMenuButton' data-toggle='dropdown'  aria-haspopup='true'"
                            + "aria-expanded='false'> Seleccione</button>"
                            + "<div class='dropdown-menu' aria-labelledby='dropdownMenuButton'>"
                            + ( objUsuario.isEstado()
                             ? "<a class='dropdown-item btn_eliminar' data-id='" + objUsuario.getId() + "' href='javascript:void(0)'>Eliminar</a>"
                            : "<a class='dropdown-item btn_activar' data-id='" +objUsuario.getId() + "' href='javascript:void(0)'>Activar</a>")
                            + "<a class='dropdown-item btn_editar' data-id='" + objUsuario.getId() + "' href='javascript:void(0)'>Actualizar</a>"
                            + "</div>"
                            + "</div>"
                            + "</td>"
                            + " </tr>";
                    i++;
                }
                html += "  </tbody>\n"
                        + "</table>";
                this.json.put("resultado", "exito");
                this.json.put("tabla", html);
                this.json.put("cantidad", cont);
                this.array.put(this.json);
                response.getWriter().write(array.toString());
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
