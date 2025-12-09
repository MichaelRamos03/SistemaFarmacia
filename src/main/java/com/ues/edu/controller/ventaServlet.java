/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controller;

import com.ues.edu.controler.persistencia.PersonaJpaControler;
import com.ues.edu.controler.persistencia.ventaJpaControler;
import com.ues.edu.entities.Persona;
import com.ues.edu.entities.Venta;
import com.ues.edu.persistencia.exception.NonexistentEntityException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
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
@WebServlet(name = "ventaServlet", urlPatterns = {"/ventaServlet"})
public class ventaServlet extends HttpServlet {

    private List<Persona> personaList;
    private Persona persona;
    private Venta Venta;
    private List<Venta> ventaList;
    private Venta ventaRecuperada;

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
            out.println("<title>Servlet ventaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ventaServlet at " + request.getContextPath() + "</h1>");
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
                String comboPersona = "";

                PersonaJpaControler personaJpaControl = new PersonaJpaControler(); // llamada al emf

                this.personaList = new ArrayList<>();
                this.personaList = personaJpaControl.findPersonaEntities();

                if (!this.personaList.isEmpty()) {
                    for (Persona persona : this.personaList) {
                        comboPersona += "<option value='" + persona.getId() + "'>" + persona.getNombrePersona() + "</option>";
                    }
                    this.json.put("persona", comboPersona);
                    this.json.put("resultado","exito");
                     this.array.put(this.json);
                }
               
                response.getWriter().write(this.array.toString());
            }
            break;

            case "insertar": {

                try {

                    Date fechaVenta = Date.valueOf(request.getParameter("fechaVenta"));

                    int idPersona = Integer.parseInt(request.getParameter("id"));
                    this.persona = new Persona(idPersona);

                    Venta objVenta = new Venta(fechaVenta, this.persona);

                    ventaJpaControler ventaJpaControl = new ventaJpaControler(); // emf

                    String resultado = ventaJpaControl.create(objVenta);

                    int idVenta = objVenta.getId(); //obteniendo el idVenta Despues de guardar
                    json.put("resultado", resultado);
                    json.put("idVenta", idVenta);

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

                    int idVenta = Integer.parseInt(request.getParameter("idVenta"));
                    Date fechaVenta = Date.valueOf(request.getParameter("fechaVenta"));

                    int idPersona = Integer.parseInt(request.getParameter("id"));
                    this.persona = new Persona(idPersona);

                    Venta objVenta = new Venta(idVenta, fechaVenta, this.persona);

                    ventaJpaControler ventaJpaControl = new ventaJpaControler();

                    String actualizar = ventaJpaControl.edit(objVenta);

                    if (actualizar.equals("exito")) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error");
                    }
                    this.array.put(this.json);
                    response.getWriter().write(this.array.toString());
                } catch (Exception ex) {
                    Logger.getLogger(ventaServlet.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            break;

            case "consultar": {

                ventaJpaControler ventaJpaControl = new ventaJpaControler(); //emf
                String html = "<table class=\"table\" id=\"tabla_venta\""
                        + "class=\"table table-bordered dt-responsive nowrap\" width=\"100%\">\n"
                        + "  <thead>\n"
                        + "    <tr>\n"
                        + "      <th scope=\"col\">#</th>\n"
                        + "      <th scope=\"col\">FechaVenta:</th>\n"
                        + "      <th scope=\"col\">Vendedor:</th>\n"
                        + "      <th scope=\"col\">Usuario:</th>\n"
                        + "      <th scope=\"col\">Acciones:</th>\n"
                        + "    </tr>\n"
                        + "  </thead>\n"
                        + "  <tbody>";
                this.ventaList = new ArrayList<>();
                this.ventaList = ventaJpaControl.findVentaEntities();
                int cont = 0;
                int i = 1;

                for (Venta objVenta : this.ventaList) {
                    cont++;
                    html += "  <tr>\n"
                            + "      <td>" + i + "</td>\n"
                            + "      <td>" + objVenta.getFechaVenta() + "</td>\n"
                            + "      <td>" + objVenta.getPersona().getNombrePersona() + "</td>\n"
                            + "      <td>" + objVenta.getPersona().getUsuario().getUsuario() + "</td>\n"
                            + "<td>"
                            + "<div class='dropdown m-b-10'>"
                            + "<button class='btn btn-secondary dropdown-toggle'"
                            + " type='button' id='dropdownMenuButton' data-toggle='dropdown'  aria-haspopup='true'"
                            + "aria-expanded='false'> Seleccione</button>"
                            + "<div class='dropdown-menu' aria-labelledby='dropdownMenuButton'>"
                            + "<a class='dropdown-item btn_eliminar' data-id='" + objVenta.getId() + "' href='javascript:void(0) '>Eliminar</a>"
                            + "<a class='dropdown-item btn_editar' data-id='" + objVenta.getId() + "' href='javascript:void(0) '>Actualizar</a>"
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
                JSONObject ventaJsonObject = new JSONObject();
                ventaJpaControler ventaModel = new ventaJpaControler(); //emf

                int idVenta = Integer.parseInt(request.getParameter("idVenta"));
                this.Venta = new Venta(); // Es indispensable antes de setear sino da error
                this.Venta.setId(idVenta);

                this.ventaRecuperada = new Venta();
                this.ventaRecuperada = ventaModel.findVenta(idVenta);
                if (this.ventaRecuperada != null) {

                    ventaJsonObject.put("idVenta", this.ventaRecuperada.getId());
                    ventaJsonObject.put("fechaVenta", this.ventaRecuperada.getFechaVenta());
                    ventaJsonObject.put("id", this.ventaRecuperada.getPersona().getId());

                    this.json.put("resultado", "exito");
                    this.json.put("venta", ventaJsonObject);
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
                    ventaJpaControler ventaModel = null;
                    ventaModel = new ventaJpaControler(); //emf
                    int idElim = Integer.parseInt(request.getParameter("idVenta"));
                    resultado = ventaModel.destroy(idElim);
                    if ("exito".equals(resultado)) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error_eliminar");
                    }
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(ventaServlet.class.getName()).log(Level.SEVERE, null, ex);
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
