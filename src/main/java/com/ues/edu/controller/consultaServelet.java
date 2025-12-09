/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controller;

import com.ues.edu.controler.persistencia.ConsultaJpaControler;
import com.ues.edu.entities.Medicamento;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Gaby Laínez
 */
@WebServlet(name = "consultaServelet", urlPatterns = {"/consultaServelet"})
public class consultaServelet extends HttpServlet {

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
            out.println("<title>Servlet consultaServelet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet consultaServelet at " + request.getContextPath() + "</h1>");
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

         SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String op = request.getParameter("opcion");
        System.out.println("OPCION EN doPost: " + op);

        if (op == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro 'opcion' es requerido");
            return;
        }

        switch (op) {
            case "consultar": {
                ConsultaJpaControler consultaJpaController = new ConsultaJpaControler();

                StringBuilder html = new StringBuilder();
                html.append("<table class=\"table table-bordered dt-responsive nowrap\" id=\"tabla\" width=\"100%\">\n")
                        .append("  <thead>\n")
                        .append("    <tr>\n")
                        .append("      <th scope=\"col\">NOMBRE</th>\n")
                        .append("      <th scope=\"col\">DESCRIPCION</th>\n")
                        .append("      <th scope=\"col\">FECHA INGRESO</th>\n")
                        .append("      <th scope=\"col\">DISPONIBLES</th>\n")
                        .append("      <th scope=\"col\">ESTADO</th>\n")
                        .append("    </tr>\n")
                        .append("  </thead>\n")
                        .append("  <tbody>\n");

                List<Medicamento> consultaList = consultaJpaController.findMedicamentoEntities();

                for (Medicamento M : consultaList) {
                
                        html.append("  <tr>\n")
                                .append("      <td>").append(M.getNombre()).append("</td>\n")
                                .append("      <td>").append(M.getDescripcion()).append("</td>\n")
                                .append("      <td>").append(formatoFecha.format(M.getFechaIngreso())).append("</td>\n")
                                .append("      <td>").append(M.getCantidadExistencias()).append("</td>\n")
                               .append("      <td>").append(M.isActivo()? "Activo" : "Inactivo").append("</td>\n")
                                .append("</td>\n");

                    
                }

                html.append("  </tbody>\n</table>");

                json = new JSONObject();
                json.put("resultado", "exito");
                json.put("tabla", html.toString());

                array = new JSONArray();
                array.put(json);

                response.getWriter().write(array.toString());
                break;
            }
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Opción no válida");
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
