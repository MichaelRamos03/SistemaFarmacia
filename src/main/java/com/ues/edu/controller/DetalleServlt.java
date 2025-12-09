/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controller;

import com.ues.edu.controler.persistencia.MedicinaJpaControler;
import com.ues.edu.controler.persistencia.UsuarioJpaControler;
import com.ues.edu.controler.persistencia.detalleJpaControler;
import com.ues.edu.controler.persistencia.ventaJpaControler;
import com.ues.edu.entities.Medicamento;
import com.ues.edu.entities.Venta;
import com.ues.edu.entities.detalleVenta;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Gaby Laínez
 */
@WebServlet(name = "DetalleServlt", urlPatterns = {"/DetalleServlt"})
public class DetalleServlt extends HttpServlet {

    private List<Medicamento> medicamentoList;
    private Medicamento medicamento;
    private detalleVenta detalleVentas;
    private List<detalleVenta> detalleVentaList;
    private detalleVenta detalleVentaRecuperada;
    private Venta venta;
    private List<Venta> ventaList;

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
            out.println("<title>Servlet DetalleServlt</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DetalleServlt at " + request.getContextPath() + "</h1>");
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
                String comboMedicamento = "";

                MedicinaJpaControler medicinaJpaControl = new MedicinaJpaControler();

                this.medicamentoList = new ArrayList<>();
                this.medicamentoList = medicinaJpaControl.findMedicamentoEntities();

                if (!this.medicamentoList.isEmpty()) {
                    for (Medicamento medicamento : this.medicamentoList) {
                        comboMedicamento += "<option value='" + medicamento.getIdMedicamento() + "'>" + medicamento.getNombre() + "</option>";
                    }
                    this.json.put("medicamentos", comboMedicamento);
                }

                if (!comboMedicamento.isEmpty()) {
                    this.json.put("resultado", "exito");
                } else {
                    this.json.put("resultado", "error");
                }

                this.array.put(this.json);
                response.getWriter().write(this.array.toString());
            }
            break;
            case "insertar": // Cuando también modifica.
            {

                try {
                    int cantidadProducto = Integer.parseInt(request.getParameter("cantidadProducto"));
                    int idVenta = Integer.parseInt(request.getParameter("idVenta"));
                    int idMedicamento = Integer.parseInt(request.getParameter("idMedicamento"));

                    if (cantidadProducto < 1) {
                       response.setContentType("application/json");
                        json.put("resultado", "error_cantidad_invalida");
                        json.put("mensaje", "La cantidad debe ser mayor o igual a 1");
                        this.array.put(json);
                        response.getWriter().write(array.toString());
                        return;
                    }

                    /// mandamos a traerl el controler de medicina para acceder a los id
                    MedicinaJpaControler medicinaControler = new MedicinaJpaControler();

                    Medicamento medicamento = medicinaControler.findMedicamento(idMedicamento);
                    double existenciaActual = medicamento.getCantidadExistencias();
                    double precioUnidad = medicamento.getPrecioUnidad();
                    double totalVendido = cantidadProducto * precioUnidad;

                    if (cantidadProducto > existenciaActual) {
                        json.put("resultado", "error_existencia");
                        json.put("mensaje", "No hay suficientes existencias para este producto");
                        this.array.put(json);
                        response.getWriter().write(array.toString());
                        return;
                    }

                    // Actualizar existencias
                    double nuevaExistencia = medicamento.getCantidadExistencias() - cantidadProducto;

                    medicamento.setCantidadExistencias(nuevaExistencia);
                    medicinaControler.edit(medicamento);

                    if (nuevaExistencia == 0) {
                        json.put("alerta", "Lote terminado");
                    } else if (nuevaExistencia <= 5) {
                        json.put("alerta", "Advertencia: Quedan pocas existencias (" + nuevaExistencia + ")");
                    }

                    this.medicamento = new Medicamento(idMedicamento);
                    this.venta = new Venta(idVenta);
                    detalleVenta objDetalle = new detalleVenta(cantidadProducto, totalVendido, this.venta, this.medicamento);

                    detalleJpaControler detalleJpaControl = new detalleJpaControler(); // emf

                    String resultado = detalleJpaControl.create(objDetalle);
                    
                    
                    json.put("resultado", resultado);

                } catch (Exception e) {

                    json.put("resultado", "error_exception");
                     json.put("mensaje", "Error al procesar datos: " + e.getMessage());
                    e.printStackTrace();

                }
                this.array.put(json);
                response.getWriter().write(array.toString());
            }
            break;

            case "si_actualizalo": {
                try // Cuando modifica.
                {

                    int idDetalle_venta = Integer.parseInt(request.getParameter("idDetalle_venta"));
                    int cantidadProducto = Integer.parseInt(request.getParameter("cantidadProducto"));
                    int idVenta = Integer.parseInt(request.getParameter("idVenta"));
                    int idMedicamento = Integer.parseInt(request.getParameter("idMedicamento"));
                    /// mandamos a traerl el controler de medicina para acceder a los id
                    MedicinaJpaControler medicinaControler = new MedicinaJpaControler();

                    Medicamento medicamento = medicinaControler.findMedicamento(idMedicamento);
                    int precioUnidad = (int) medicamento.getPrecioUnidad();
                    int totalVendido = cantidadProducto * precioUnidad;

                    // Actualizar existencias
                    double nuevaExistencia = medicamento.getCantidadExistencias() - cantidadProducto;

                    medicamento.setCantidadExistencias(nuevaExistencia);
                    medicinaControler.edit(medicamento);
                    this.medicamento = new Medicamento(idMedicamento);
                    this.venta = new Venta(idVenta);

                    detalleVenta objDetalle = new detalleVenta(idDetalle_venta, cantidadProducto, totalVendido, this.venta, this.medicamento);

                    detalleJpaControler detalleJpaControl = new detalleJpaControler();

                    String actualizar = detalleJpaControl.edit(objDetalle);
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
                int idVenta = Integer.parseInt(request.getParameter("idVenta"));

                detalleJpaControler detalleJpaControl = new detalleJpaControler();
                this.detalleVentaList = detalleJpaControl.findDetalleVentaByIdVenta(idVenta);

                String html = "<table class=\"table\" id=\"tabla_Detalle\""
                        + " class=\"table table-bordered dt-responsive nowrap\" width=\"100%\">\n"
                        + "  <thead>\n"
                        + "    <tr>\n"
                        + "      <th scope=\"col\">#</th>\n"
                        + "      <th scope=\"col\">Vendedor</th>\n"
                        + "      <th scope=\"col\">Nombre Medicamento</th>\n"
                        + "      <th scope=\"col\">Cantidad</th>\n"
                        + "      <th scope=\"col\">PrecioTotal</th>\n"
                        + "      <th scope=\"col\">fechaVenta</th>\n"
                        + "      <th scope=\"col\">Acciones</th>\n"
                        + "    </tr>\n"
                        
                        + "  </thead>\n"
                        + "  <tbody>";
                
                double total = 0;
                int i = 1;
                for (detalleVenta objDetalle : this.detalleVentaList) {
              
                    html += "  <tr>\n"
                            + "      <td>" + i + "</td>\n"
                            + "      <td>" + objDetalle.getVenta().getPersona().getNombrePersona() + "</td>\n"
                            + "      <td>" + objDetalle.getMedicamento().getNombre() + "</td>\n"
                            + "      <td>" + objDetalle.getCantidadProducto() + "</td>\n"
                            + "      <td>" + objDetalle.getTotalVendido() + "</td>\n"
                            + "      <td>" + objDetalle.getVenta().getFechaVenta() + "</td>\n"
                            + "      <td>"
                            + "      <div class='dropdown m-b-10'>"
                            + "      <button class='btn btn-secondary dropdown-toggle'"
                            + "      type='button' id='dropdownMenuButton' data-toggle='dropdown' aria-haspopup='true'"
                            + "      aria-expanded='false'>Seleccione</button>"
                            + "      <div class='dropdown-menu' aria-labelledby='dropdownMenuButton'>"
                            + "      <a class='dropdown-item btn_eliminar' data-id='" + objDetalle.getIdDetalle_venta() + "' href='javascript:void(0)'>Eliminar</a>"
                            + "      <a class='dropdown-item btn_editar' data-id='" + objDetalle.getIdDetalle_venta() + "' href='javascript:void(0)'>Actualizar</a>"
                            + "      </div>"
                            + "      </div>"
                            + "      </td>"
                            + "  </tr>";

                    i++;
                    total += objDetalle.getTotalVendido();
       
                }
             
                System.out.println("________________________________" +total);
                
                html += "  </tbody>\n"
                        + "  <tfoot>\n"
                        + "    <tr>\n"
                        + "      <td colspan=\"4\"><strong>Total a Pagar:</strong></td>\n"
                        + "      <td id=\"total_general\"><strong>" + total + "</strong></td>\n"
                        + "      <td colspan=\"2\"></td>\n"
                        + "    </tr>\n"
                        + "  </tfoot>\n"
                        + "</table>";
                
                html += "  </tbody>\n"
                        + "</table>";
 
                this.json.put("resultado", "exito");
                this.json.put("tabla", html);
                this.json.put("total", total);
                this.json.put("cantidad", this.detalleVentaList.size());

                this.array.put(this.json);

                System.out.println("JSON generado: " + array.toString());
                response.getWriter().write(array.toString());
            }
            break;

            case "eliminar": {
                try {
                    String resultado = "";
                    detalleJpaControler detalleModel = null;
                    detalleModel = new detalleJpaControler();
                    int idElim = Integer.parseInt(request.getParameter("idDetalle_venta"));
                    resultado = detalleModel.destroy(idElim);
                    if ("exito".equals(resultado)) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error_eliminar");
                    }
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(DetalleServlt.class.getName()).log(Level.SEVERE, null, ex);
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
