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

    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); // Formato más común

    request.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String op = request.getParameter("opcion");
    System.out.println("OPCION EN doPost: " + op);

    if (op == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro 'opcion' es requerido");
        return;
    }

    // Variables JSON
    JSONArray array = new JSONArray();
    JSONObject json = new JSONObject();

    switch (op) {
        case "consultar": {
            ConsultaJpaControler consultaJpaController = new ConsultaJpaControler();

            StringBuilder html = new StringBuilder();
            
            // Tabla con diseño Bootstrap 5 (table-hover, borderless header)
            html.append("<table class=\"table table-hover table-bordered nowrap\" id=\"tabla\" width=\"100%\">\n")
                    .append("  <thead class=\"bg-light\">\n")
                    .append("    <tr>\n")
                    .append("      <th scope=\"col\">Nombre</th>\n")
                    .append("      <th scope=\"col\">Descripción</th>\n")
                    .append("      <th scope=\"col\" class=\"text-center\">Fecha Ingreso</th>\n")
                    .append("      <th scope=\"col\" class=\"text-center\">Stock</th>\n")
                    .append("      <th scope=\"col\" class=\"text-center\">Estado</th>\n")
                    .append("    </tr>\n")
                    .append("  </thead>\n")
                    .append("  <tbody>\n");

            List<Medicamento> consultaList = consultaJpaController.findMedicamentoEntities();

            if (consultaList != null) {
                for (Medicamento M : consultaList) {
                    
                    // Badge para el estado (Verde/Rojo)
                    String estadoBadge = M.isActivo()
                            ? "<span class='badge bg-success bg-opacity-10 text-success border border-success px-3 rounded-pill'>Activo</span>"
                            : "<span class='badge bg-danger bg-opacity-10 text-danger border border-danger px-3 rounded-pill'>Inactivo</span>";

                    // Badge para Stock bajo (Opcional visual enhancement)
                    String stockClass = (M.getCantidadExistencias() < 10) ? "text-danger fw-bold" : "fw-bold";

                    html.append("  <tr>\n")
                            .append("      <td class='align-middle'>").append(M.getNombre()).append("</td>\n")
                            .append("      <td class='align-middle text-muted small'>").append(M.getDescripcion()).append("</td>\n")
                            .append("      <td class='align-middle text-center'>").append(formatoFecha.format(M.getFechaIngreso())).append("</td>\n")
                            .append("      <td class='align-middle text-center ").append(stockClass).append("'>").append(M.getCantidadExistencias()).append("</td>\n")
                            .append("      <td class='align-middle text-center'>").append(estadoBadge).append("</td>\n")
                            .append("  </tr>\n");
                }
            }

            html.append("  </tbody>\n</table>");

            json.put("resultado", "exito");
            json.put("tabla", html.toString());

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
