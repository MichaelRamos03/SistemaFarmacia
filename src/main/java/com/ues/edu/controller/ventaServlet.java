package com.ues.edu.controller;

import com.ues.edu.controler.persistencia.PersonaJpaControler;
import com.ues.edu.controler.persistencia.ventaJpaControler;
// IMPORTANTE: Importamos el controlador de detalles y la entidad
import com.ues.edu.controler.persistencia.detalleJpaControler;
import com.ues.edu.entities.Persona;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "ventaServlet", urlPatterns = {"/ventaServlet"})
public class ventaServlet extends HttpServlet {

    private List<Persona> personaList;
    private Persona persona;
    private Venta Venta;
    private List<Venta> ventaList;
    private Venta ventaRecuperada;
    private JSONArray array;
    private JSONObject json;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><head><title>Servlet</title></head><body>");
            out.println("<h1>Servlet ventaServlet</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String op = request.getParameter("opcion");
        
        this.json = new JSONObject();
        this.array = new JSONArray();
        
        switch (op) {
            case "cargarCombos": {
                String comboPersona = "";
                PersonaJpaControler personaJpaControl = new PersonaJpaControler(); 
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
                    ventaJpaControler ventaJpaControl = new ventaJpaControler(); 
                    String resultado = ventaJpaControl.create(objVenta);
                    int idVenta = objVenta.getId(); 
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
                try {
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
                    Logger.getLogger(ventaServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case "consultar": {
                ventaJpaControler ventaJpaControl = new ventaJpaControler(); 
                String html = "<table class=\"table\" id=\"tabla_venta\""
                        + "class=\"table table-bordered dt-responsive nowrap\" width=\"100%\">\n"
                        + "  <thead>\n"
                        + "    <tr>\n"
                        + "      <th scope=\"col\">#</th>\n"
                        + "      <th scope=\"col\">FechaVenta:</th>\n"
                        + "      <th scope=\"col\">Vendedor:</th>\n"
                        + "      <th scope=\"col\">Usuario:</th>\n"
                        + "      <th scope=\"col\" style='min-width: 250px;'>Acciones:</th>\n" 
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
                            + "      <td>"
                            
                            // === BOTÓN FACTURA (Con opciones) ===
                            + "         <div class='dropdown m-b-10' style='display:inline-block; vertical-align: middle; margin-right: 5px;'>"
                            + "             <button class='btn btn-danger btn-sm dropdown-toggle'"
                            + "                 type='button' id='ddFactura" + objVenta.getId() + "' data-toggle='dropdown' aria-haspopup='true'"
                            + "                 aria-expanded='false'> Factura</button>"
                            + "             <div class='dropdown-menu' aria-labelledby='ddFactura" + objVenta.getId() + "'>"
                            + "                 <a class='dropdown-item' href='javascript:void(0)' onclick='imprimirFactura(" + objVenta.getId() + ", \"fiscal\")'>Crédito Fiscal</a>"
                            + "                 <a class='dropdown-item' href='javascript:void(0)' onclick='imprimirFactura(" + objVenta.getId() + ", \"final\")'>Consumidor Final</a>"
                            + "             </div>"
                            + "         </div>"
                            
                            // === BOTÓN ACCIONES ===
                            + "         <div class='dropdown m-b-10' style='display:inline-block; vertical-align: middle;'>"
                            + "             <button class='btn btn-secondary dropdown-toggle btn-sm'"
                            + "                 type='button' id='dropdownMenuButton' data-toggle='dropdown' aria-haspopup='true'"
                            + "                 aria-expanded='false'> Seleccione</button>"
                            + "             <div class='dropdown-menu' aria-labelledby='dropdownMenuButton'>"
                            + "                 <a class='dropdown-item btn_eliminar' data-id='" + objVenta.getId() + "' href='javascript:void(0) '>Eliminar</a>"
                            + "                 <a class='dropdown-item btn_editar' data-id='" + objVenta.getId() + "' href='javascript:void(0) '>Actualizar</a>"
                            + "             </div>"
                            + "         </div>"
                            + "      </td>"
                            + " </tr>";
                    i++;
                }
                html += "  </tbody></table>";
                this.json.put("resultado", "exito");
                this.json.put("tabla", html);
                this.json.put("cantidad", cont);
                this.array.put(this.json);
                response.getWriter().write(array.toString());
            }
            break;

            case "editar_consultar": {
                JSONObject ventaJsonObject = new JSONObject();
                ventaJpaControler ventaModel = new ventaJpaControler(); 
                int idVenta = Integer.parseInt(request.getParameter("idVenta"));
                this.Venta = new Venta(); 
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
                    ventaJpaControler ventaModel = new ventaJpaControler(); 
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
            
            // =========================================================================
            // AQUÍ ESTÁ LA LÓGICA DE LA FACTURA REAL (CON DATOS DE LA BD Y VISTA PREVIA)
            // =========================================================================
            case "generarFactura": {
                int idVenta = Integer.parseInt(request.getParameter("idVenta"));
                String tipoDocumento = request.getParameter("tipoDocumento");
                
                // 1. Buscamos la Venta Principal (Encabezado)
                ventaJpaControler ventaJpaControl = new ventaJpaControler(); 
                Venta ventaEncontrada = ventaJpaControl.findVenta(idVenta);
                
                // 2. Buscamos los Detalles de esa Venta (Productos)
                detalleJpaControler detalleControl = new detalleJpaControler();
                List<detalleVenta> listaDetalles = detalleControl.findDetalleVentaByIdVenta(idVenta);
                
                String htmlFactura = "";
                
                if (ventaEncontrada != null) {
                    // Configuración Visual según tipo
                    String titulo = (tipoDocumento.equals("fiscal")) ? "COMPROBANTE DE CRÉDITO FISCAL" : "FACTURA CONSUMIDOR FINAL";
                    String colorHeader = (tipoDocumento.equals("fiscal")) ? "#8B0000" : "#ea553d"; // Rojo Oscuro vs Naranja
                    
                    // --- Construcción de Filas de Productos ---
                    StringBuilder filasHtml = new StringBuilder();
                    double totalFactura = 0.0;
                    
                    if(listaDetalles != null && !listaDetalles.isEmpty()){
                        for(detalleVenta det : listaDetalles){
                            filasHtml.append("<tr>");
                            // Asumiendo que detalleVenta tiene relación con Medicamento
                            filasHtml.append("<td>").append(det.getMedicamento().getNombre()).append("</td>"); 
                            filasHtml.append("<td style='text-align:center'>").append(det.getCantidadProducto()).append("</td>");
                            
                            // Calculamos precio unitario (Total / Cantidad) para mostrarlo
                            double precioUnitario = det.getTotalVendido() / det.getCantidadProducto();
                            filasHtml.append("<td style='text-align:right'>$").append(String.format("%.2f", precioUnitario)).append("</td>");
                            
                            filasHtml.append("<td style='text-align:right'>$").append(String.format("%.2f", det.getTotalVendido())).append("</td>");
                            filasHtml.append("</tr>");
                            
                            totalFactura += det.getTotalVendido();
                        }
                    } else {
                        filasHtml.append("<tr><td colspan='4' style='text-align:center'>No hay productos registrados en esta venta</td></tr>");
                    }

                    // --- Construcción del HTML Completo (Con estilos para Imprimir/Cerrar) ---
                    htmlFactura += "<html><head><title>" + titulo + " #" + idVenta + "</title>";
                    htmlFactura += "<style>";
                    htmlFactura += "body { font-family: 'Helvetica', Arial, sans-serif; padding: 30px; color: #333; }";
                    // CSS para ocultar botones al imprimir
                    htmlFactura += "@media print { .no-print { display: none !important; } }";
                    htmlFactura += ".header { display: flex; justify-content: space-between; border-bottom: 3px solid " + colorHeader + "; padding-bottom: 10px; margin-bottom: 20px; }";
                    htmlFactura += ".empresa h1 { color: " + colorHeader + "; margin: 0; font-size: 24px; }";
                    htmlFactura += "table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }";
                    htmlFactura += "th { background-color: #333; color: white; padding: 10px; text-align: left; }";
                    htmlFactura += "td { border-bottom: 1px solid #ddd; padding: 8px; }";
                    htmlFactura += ".total { text-align: right; font-size: 20px; font-weight: bold; color: " + colorHeader + "; margin-top: 15px; border-top: 2px solid #333; padding-top:10px; }";
                    htmlFactura += ".btn-accion { padding: 8px 15px; color: white; border: none; cursor: pointer; border-radius: 4px; font-size: 14px; margin-left: 10px; text-decoration: none; display: inline-block; font-weight: bold; }";
                    htmlFactura += "</style></head><body>";
                    
                    // BARRA DE HERRAMIENTAS (Vista Previa)
                    htmlFactura += "<div class='no-print' style='background: #f1f1f1; padding: 10px; margin-bottom: 20px; border-radius: 5px; text-align: right; border: 1px solid #ccc;'>";
                    htmlFactura += "<span style='float:left; font-weight:bold; padding-top:5px; color:#555;'>VISTA PREVIA</span>";
                    htmlFactura += "<button onclick='window.print()' class='btn-accion' style='background-color: #28a745;'>🖨️ IMPRIMIR</button>";
                    htmlFactura += "<button onclick='window.close()' class='btn-accion' style='background-color: #dc3545;'>❌ CERRAR</button>";
                    htmlFactura += "</div>";
                    
                    htmlFactura += "<div class='header'>";
                    htmlFactura += "  <div class='empresa'><h1>MI FARMACIA S.A. DE C.V.</h1><p>Sucursal Central<br>Tel: 2222-0000</p></div>";
                    htmlFactura += "  <div style='text-align:right'><h3>" + titulo + "</h3><p><strong>N° Doc:</strong> " + String.format("%05d", idVenta) + "</p><p><strong>Fecha:</strong> " + ventaEncontrada.getFechaVenta() + "</p></div>";
                    htmlFactura += "</div>";
                    
                    htmlFactura += "<div style='background:#f9f9f9; padding:15px; margin-bottom:20px; border-radius: 5px;'>";
                    htmlFactura += "  <p><strong>Cliente:</strong> Cliente General</p>";
                    htmlFactura += "  <p><strong>Vendedor:</strong> " + ventaEncontrada.getPersona().getNombrePersona() + "</p>";
                    if(tipoDocumento.equals("fiscal")) {
                         htmlFactura += "<p><strong>NRC:</strong> 000000-0 &nbsp;&nbsp; <strong>NIT:</strong> 0000-000000-000-0</p>";
                    }
                    htmlFactura += "</div>";
                    
                    htmlFactura += "<table>";
                    htmlFactura += "<thead><tr><th>Medicamento</th><th style='text-align:center'>Cant.</th><th style='text-align:right'>Precio Unit.</th><th style='text-align:right'>Total</th></tr></thead>";
                    htmlFactura += "<tbody>" + filasHtml.toString() + "</tbody>";
                    htmlFactura += "</table>";
                    
                    htmlFactura += "<div class='total'>TOTAL A PAGAR: $" + String.format("%.2f", totalFactura) + "</div>";
                    
                    htmlFactura += "</body></html>";
                    
                    this.json.put("resultado", "exito");
                    this.json.put("html", htmlFactura);
                } else {
                    this.json.put("resultado", "error");
                    this.json.put("mensaje", "No se encontró la venta.");
                }
                this.array.put(this.json);
                response.getWriter().write(array.toString());
            }
            break;
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}