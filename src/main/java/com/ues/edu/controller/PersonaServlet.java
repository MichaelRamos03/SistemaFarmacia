/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controller;

import com.ues.edu.controler.persistencia.PersonaJpaControler;
import com.ues.edu.controler.persistencia.UsuarioJpaControler;
import com.ues.edu.entities.Persona;
import com.ues.edu.entities.Usuario;
import com.ues.edu.persistencia.exception.NonexistentEntityException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Gaby Laínez
 */
@WebServlet(name = "PersonaServlet", urlPatterns = {"/PersonaServlet"})
public class PersonaServlet extends HttpServlet {

    private List<Usuario> usuariList;
    private List<Persona> personaList;
    private Usuario usuario;
    private Persona persona;
    private Persona personsRecuperada;

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
            out.println("<title>Servlet PersonaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PersonaServlet at " + request.getContextPath() + "</h1>");
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
                String comboUsuario = "";

                UsuarioJpaControler usuarioJpaControl = new UsuarioJpaControler(); // llamada al emf

                this.usuariList = new ArrayList<>();
                this.usuariList = usuarioJpaControl.findUsuarioEntities();

                if (!this.usuariList.isEmpty()) {
                    for (Usuario usuario : this.usuariList) {
                        comboUsuario += "<option value='" + usuario.getId() + "'>" + usuario.getUsuario() + "</option>";
                    }
                    this.json.put("usuario", comboUsuario);
                }
                this.array.put(this.json);
                response.getWriter().write(this.array.toString());
            }
            break;

            case "insertar": {

                try {

                    String nombre = request.getParameter("nombre");

                    Date fechaNacimiento = Date.valueOf(request.getParameter("fechaNacimiento"));
                    LocalDate nacimiento = fechaNacimiento.toLocalDate();
                    LocalDate hoy = LocalDate.now();
                    int edad = Period.between(nacimiento, hoy).getYears();

                    int dui = Integer.parseInt(request.getParameter("dui"));
                    String telefono = request.getParameter("telefono");

                    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                    this.usuario = new Usuario(idUsuario);

                    Persona objPersona = new Persona(nombre, fechaNacimiento, edad, dui, telefono, this.usuario);

                    PersonaJpaControler personaJpaControl = new PersonaJpaControler(); // emf

                    String resultado = personaJpaControl.create(objPersona);
                    json.put("resultado", resultado);

                } catch (Exception e) {
                    json.put("resultado", "error_exception");
                    e.printStackTrace();
                }
                this.array.put(this.json);
                response.getWriter().write(array.toString());
            }
            break;

            case "si_actualizalo": {
                try // Cuando modifica.
                {
                    int idPersona = Integer.parseInt(request.getParameter("idPersona"));
                    String nombre = request.getParameter("nombre");
                    Date fechaNacimiento = Date.valueOf(request.getParameter("fechaNacimiento"));
                    //Recalculando Edad
                    LocalDate nacimiento = fechaNacimiento.toLocalDate();
                    LocalDate hoy = LocalDate.now();
                    int edad = Period.between(nacimiento, hoy).getYears();

                    int dui = Integer.parseInt(request.getParameter("dui"));
                    String telefono = request.getParameter("telefono");

                    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                    this.usuario = new Usuario(idUsuario);

                    Persona objPersona = new Persona(idPersona, nombre, fechaNacimiento, edad, dui, telefono, this.usuario);

                    PersonaJpaControler personaJpaControl = new PersonaJpaControler(); // emf

                    String actualizar = personaJpaControl.edit(objPersona);
                    if (actualizar.equals("exito")) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error");
                    }
                    this.array.put(this.json);
                    response.getWriter().write(this.array.toString());
                } catch (Exception ex) {
                    Logger.getLogger(PersonaServlet.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            break;

            case "consultar": {

                PersonaJpaControler personaJpaControl = new PersonaJpaControler(); //emf
                String html = "<table class=\"table\" id=\"tabla_persona\""
                        + "class=\"table table-bordered dt-responsive nowrap\" width=\"100%\">\n"
                        + "  <thead>\n"
                        + "    <tr>\n"
                        + "      <th scope=\"col\">#</th>\n"
                        + "      <th scope=\"col\">Usuario:</th>\n"
                        + "      <th scope=\"col\">Nombre:</th>\n"
                        + "      <th scope=\"col\">Edad:</th>\n"
                        + "      <th scope=\"col\">Dui:</th>\n"
                        + "      <th scope=\"col\">Telefono:</th>\n"
                        + "      <th scope=\"col\">Acciones:</th>\n"
                        + "    </tr>\n"
                        + "  </thead>\n"
                        + "  <tbody>";
                this.personaList = new ArrayList<>();
                this.personaList = personaJpaControl.findPersonaEntities();
                int cont = 0;
                int i = 1;

                for (Persona objPersona : this.personaList) {
                    cont++;
                    html += "  <tr>\n"
                            + "      <td>" + i + "</td>\n"
                            + "      <td>" + objPersona.getUsuario().getUsuario() + "</td>\n"
                            + "      <td>" + objPersona.getNombrePersona() + "</td>\n"
                            + "      <td>" + objPersona.getEdad() + "</td>\n"
                            + "      <td>" + objPersona.getDui() + "</td>\n"
                            + "      <td>" + objPersona.getTelefono() + "</td>\n"
                            + "<td>"
                            + "<div class='dropdown m-b-10'>"
                            + "<button class='btn btn-secondary dropdown-toggle'"
                            + " type='button' id='dropdownMenuButton' data-toggle='dropdown'  aria-haspopup='true'"
                            + "aria-expanded='false'> Seleccione</button>"
                            + "<div class='dropdown-menu' aria-labelledby='dropdownMenuButton'>"
                            + "<a class='dropdown-item btn_eliminar' data-id='" + objPersona.getId() + "' href='javascript:void(0) '>Eliminar</a>"
                            + "<a class='dropdown-item btn_editar' data-id='" + objPersona.getId() + "' href='javascript:void(0) '>Actualizar</a>"
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
                JSONObject personaJsonObject = new JSONObject();
                PersonaJpaControler personaModel = new PersonaJpaControler(); //emf
                
                int idPersona = Integer.parseInt(request.getParameter("id"));
                this.persona = new Persona(); // Es indispensable antes de setear sino da error
                this.persona.setId(idPersona);

                this.personsRecuperada = new Persona();
                this.personsRecuperada = personaModel.findPersona(idPersona);
                if (this.personsRecuperada != null) {
                    personaJsonObject.put("idPersona", this.personsRecuperada.getId());
                    personaJsonObject.put("nombre", this.personsRecuperada.getNombrePersona());
                  
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaFormateada = sdf.format(this.personsRecuperada.getFechaNacimiento());
                    personaJsonObject.put("fechaNacimiento", fechaFormateada);
                   
                    personaJsonObject.put("dui", this.personsRecuperada.getDui());
                    personaJsonObject.put("telefono", this.personsRecuperada.getTelefono());
                    personaJsonObject.put("idUsuario", this.personsRecuperada.getUsuario().getId());

                    this.json.put("resultado", "exito");
                    this.json.put("persona", personaJsonObject);
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
                    PersonaJpaControler personaModel = null;
                    personaModel = new PersonaJpaControler(); //emf
                    int idElim = Integer.parseInt(request.getParameter("id"));
                    resultado = personaModel.destroy(idElim);
                    if ("exito".equals(resultado)) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error_eliminar");
                    }
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(PersonaServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.array.put(this.json);
            response.getWriter().write(this.array.toString());
            break;
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
