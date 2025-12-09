/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controller;

import com.ues.edu.controler.persistencia.MedicinaJpaControler;
import com.ues.edu.controler.persistencia.PersonaJpaControler;
import com.ues.edu.controler.persistencia.UsuarioJpaControler;
import com.ues.edu.controler.persistencia.categoriaJpaControler;
import com.ues.edu.entities.Categoria;
import com.ues.edu.entities.Medicamento;
import com.ues.edu.entities.Persona;
import com.ues.edu.persistencia.exception.NonexistentEntityException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
@WebServlet(name = "medicinaServlet", urlPatterns = {"/medicinaServlet"})
public class medicinaServlet extends HttpServlet {

    private List<Medicamento> medicinaList;
    private List<Categoria> categoriaLit;
    private Categoria categoria;
    private Medicamento medicina;
    private Medicamento medicinaRecuperada;

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

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet medicinaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet medicinaServlet at " + request.getContextPath() + "</h1>");
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
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        //fomrato desde html
        SimpleDateFormat formatoFe = new SimpleDateFormat("yyyy-MM-dd");

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String op = request.getParameter("opcion");
        System.out.println("OPCION EN doPost " + op);
        switch (op) {
            case "cargarCombos": {
                String comboCategoria = "";
                categoriaJpaControler categoriaJpaControl = new categoriaJpaControler(); // llamada al emf
                this.categoriaLit = new ArrayList<>();
                this.categoriaLit = categoriaJpaControl.findCategoriaEntities();
                if (!this.categoriaLit.isEmpty()) {
                    for (Categoria categoria : this.categoriaLit) {
                        comboCategoria += "<option value=" + categoria.getIdCategoria() + ">"
                                + categoria.getNombreCategoria() + "</option>";
                    }
                    this.json.put("resultado", "exito");
                    this.json.put("categoria", comboCategoria);
                    this.array.put(this.json);
                } else {
                    this.json.put("resultado", "error");
                }
                response.getWriter().write(this.array.toString());
            }
            break;

            case "insertar": {
                try {

                    String nombre = request.getParameter("nombre");
                    int cantidadExistencias = Integer.parseInt(request.getParameter("cantidadExistencias"));
                    double precioUnidad = Double.parseDouble(request.getParameter("precioUnidad"));
                    double precioTotal = (cantidadExistencias * precioUnidad);
                    Date fechaIngreso = Date.valueOf(request.getParameter("fechaIngreso"));
                    Date fechaDeExpiracion = Date.valueOf(request.getParameter("fechaDeExpiracion"));
                    String descripcion = request.getParameter("descripcion");

                    int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                    String estadoStr = request.getParameter("activo");
                    boolean activo = "activo".equalsIgnoreCase(estadoStr);

                    this.categoria = new Categoria(idCategoria);

                    Medicamento objMedicamento = new Medicamento(nombre, cantidadExistencias, precioUnidad, precioTotal, fechaIngreso, fechaDeExpiracion, descripcion, activo, this.categoria);

                    MedicinaJpaControler medicinaJpaControl = new MedicinaJpaControler(); // emf

                    String resultado = medicinaJpaControl.create(objMedicamento);
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
                    int idMedicamento = Integer.parseInt(request.getParameter("idMedicamento"));
                    String nombre = request.getParameter("nombre");
                    int cantidadExistencias = Integer.parseInt(request.getParameter("cantidadExistencias"));
                    double precioUnidad = Double.parseDouble(request.getParameter("precioUnidad"));
                    double precioTotal = (cantidadExistencias * precioUnidad);

                    Date fechaIngreso = Date.valueOf(request.getParameter("fechaIngreso"));
                    Date fechaDeExpiracion = Date.valueOf(request.getParameter("fechaDeExpiracion"));
                    String descripcion = request.getParameter("descripcion");

                    int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                    String estadoStr = request.getParameter("activo");
                    boolean activo = "activo".equalsIgnoreCase(estadoStr);
                    this.categoria = new Categoria(idCategoria);

                    Medicamento objMedicamento = new Medicamento(idMedicamento, nombre, cantidadExistencias, precioUnidad, precioTotal, fechaIngreso, fechaDeExpiracion, descripcion, activo, this.categoria);

                    MedicinaJpaControler medicinaJpaControl = new MedicinaJpaControler();

                    String actualizar = medicinaJpaControl.edit(objMedicamento);

                    if (actualizar.equals("exito")) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error");
                    }
                    this.array.put(this.json);
                    response.getWriter().write(this.array.toString());
                } catch (Exception ex) {
                    Logger.getLogger(medicinaServlet.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            break;

            // ... (Tus imports existentes) ...
// DENTRO DEL MÉTODO doPost Y EL SWITCH:
            case "consultar": {
                MedicinaJpaControler medicinaJpaControl = new MedicinaJpaControler();

                // Formato fecha local (para evitar duplicados)
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                String html = "<table id=\"tabla_medicamentos\" class=\"table table-hover table-bordered nowrap\" style=\"width:100%\">\n"
                        + "  <thead class=\"bg-light\">\n"
                        + "    <tr>\n"
                        + "      <th scope=\"col\">#</th>\n"
                        + "      <th scope=\"col\">Nombre</th>\n"
                        + "      <th scope=\"col\">Categoría</th>\n"
                        + "      <th scope=\"col\">Stock</th>\n"
                        + "      <th scope=\"col\">Precio U.</th>\n"
                        + "      <th scope=\"col\">Total</th>\n"
                        + "      <th scope=\"col\">Vence</th>\n"
                        + "      <th scope=\"col\" class=\"text-center\">Estado</th>\n"
                        + "      <th scope=\"col\" class=\"text-center\">Acciones</th>\n"
                        + "    </tr>\n"
                        + "  </thead>\n"
                        + "  <tbody>";

                try {
                    this.medicinaList = medicinaJpaControl.findMedicamentoEntities();
                } catch (Exception e) {
                    this.medicinaList = new ArrayList<>();
                }

                int cont = 0;
                int i = 1;

                if (this.medicinaList != null) {
                    for (Medicamento obj : this.medicinaList) {
                        // Solo mostramos activos en la tabla principal si queremos separar papelera
                        // Pero si tu lógica trae todos, filtramos visualmente o mostramos todos
                        // Asumiré que quieres ver los activos aquí
                        if (obj.isActivo()) {
                            cont++;
                            String nombre = (obj.getNombre() != null) ? obj.getNombre() : "Sin nombre";
                            String catName = (obj.getCategoria() != null) ? obj.getCategoria().getNombreCategoria() : "-";
                            String fechaVence = (obj.getFechaDeExpiracion() != null) ? sdf.format(obj.getFechaDeExpiracion()) : "-";

                            html += "  <tr>\n"
                                    + "      <td class='align-middle fw-bold'>" + i + "</td>\n"
                                    + "      <td class='align-middle'>" + nombre + "</td>\n"
                                    + "      <td class='align-middle'><span class='badge bg-light text-dark border'>" + catName + "</span></td>\n"
                                    + "      <td class='align-middle fw-bold'>" + obj.getCantidadExistencias() + "</td>\n"
                                    + "      <td class='align-middle'>$" + obj.getPrecioUnidad() + "</td>\n"
                                    + "      <td class='align-middle text-primary'>$" + obj.getPrecioTotal() + "</td>\n"
                                    + "      <td class='align-middle'>" + fechaVence + "</td>\n"
                                    + "      <td class='align-middle text-center'><span class='badge bg-success bg-opacity-10 text-success px-3 border border-success'>Activo</span></td>\n"
                                    + "      <td class='align-middle text-center'>\n"
                                    + "        <div class='d-flex justify-content-center gap-2'>\n"
                                    + "          <button class='btn btn-sm btn-outline-purple btn_editar' data-id='" + obj.getIdMedicamento() + "' title='Editar'>\n"
                                    + "            <i class='bi bi-pencil-fill'></i>\n"
                                    + "          </button>\n"
                                    + "          <button class='btn btn-sm btn-outline-danger btn_eliminar' data-id='" + obj.getIdMedicamento() + "' title='Eliminar'>\n"
                                    + "            <i class='bi bi-trash-fill'></i>\n"
                                    + "          </button>\n"
                                    + "        </div>\n"
                                    + "      </td>\n"
                                    + "  </tr>";
                            i++;
                        }
                    }
                }

                html += "  </tbody>\n" + "</table>";

                this.json.put("resultado", "exito");
                this.json.put("tabla", html);
                this.json.put("cantidad", cont);
                this.array.put(this.json);
                response.getWriter().write(this.array.toString());
            }
            break;

            case "listar_inactivas": {
                MedicinaJpaControler medicinaJpaControl = new MedicinaJpaControler();
                // Asumimos que tienes un método similar a findMedicamentoInactivos o traes todos y filtras
                List<Medicamento> lista = medicinaJpaControl.findMedicamentoEntities(); // Traemos todos

                JSONArray listaJson = new JSONArray();
                if (lista != null) {
                    for (Medicamento m : lista) {
                        if (!m.isActivo()) { // Filtramos los inactivos
                            JSONObject obj = new JSONObject();
                            obj.put("idMedicamento", m.getIdMedicamento());
                            obj.put("nombre", m.getNombre());
                            obj.put("stock", m.getCantidadExistencias());
                            listaJson.put(obj);
                        }
                    }
                }
                response.getWriter().write(listaJson.toString());
                break;
            }

            case "reactivar": {
                MedicinaJpaControler medicinaJpaControl = new MedicinaJpaControler();
                int id = Integer.parseInt(request.getParameter("idMedicamento"));

                // Lógica de reactivación manual si no tienes el método específico
                // Recuperamos, cambiamos estado y editamos
                try {
                    Medicamento med = medicinaJpaControl.findMedicamento(id);
                    if (med != null) {
                        med.setActivo(true); // Reactivamos
                        medicinaJpaControl.edit(med);
                        json.put("resultado", "exito");
                    } else {
                        json.put("resultado", "error");
                    }
                } catch (Exception ex) {
                    json.put("resultado", "error");
                    ex.printStackTrace();
                }

                this.array.put(json);
                response.getWriter().write(this.array.toString());
                break;
            }

            case "editar_consultar": {
                JSONObject medicamentoJsonObject = new JSONObject();
                MedicinaJpaControler medicinaModel = new MedicinaJpaControler(); //emf
                int idMedicamento = Integer.parseInt(request.getParameter("idMedicamento"));
                this.medicina = new Medicamento(); // Es indispensable antes de setear sino da error
                this.medicina.setIdMedicamento(idMedicamento);

                this.medicinaRecuperada = new Medicamento();
                this.medicinaRecuperada = medicinaModel.findMedicamento(idMedicamento);
                if (this.medicinaRecuperada != null) {
                    medicamentoJsonObject.put("idMedicamento", this.medicinaRecuperada.getIdMedicamento());
                    medicamentoJsonObject.put("nombre", this.medicinaRecuperada.getNombre());
                    medicamentoJsonObject.put("cantidadExistencias", this.medicinaRecuperada.getCantidadExistencias());
                    medicamentoJsonObject.put("precioUnidad", this.medicinaRecuperada.getPrecioUnidad());
                    medicamentoJsonObject.put("precioTotal", this.medicinaRecuperada.getPrecioTotal());
                    medicamentoJsonObject.put("fechaIngreso", formatoFe.format(medicinaRecuperada.getFechaIngreso()));
                    medicamentoJsonObject.put("fechaDeExpiracion", formatoFe.format(medicinaRecuperada.getFechaDeExpiracion()));
                    medicamentoJsonObject.put("descripcion", this.medicinaRecuperada.getDescripcion());
                    medicamentoJsonObject.put("activo", this.medicinaRecuperada.isActivo() ? "activo" : "inactivo");

                    medicamentoJsonObject.put("idCategoria", this.medicinaRecuperada.getCategoria().getIdCategoria());

                    this.json.put("resultado", "exito");
                    this.json.put("medicina", medicamentoJsonObject);
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
                    MedicinaJpaControler medicinaModel = null;
                    medicinaModel = new MedicinaJpaControler(); //emf
                    int idElim = Integer.parseInt(request.getParameter("idMedicamento"));
                    resultado = medicinaModel.destroy(idElim);
                    if ("exito".equals(resultado)) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error_eliminar");
                    }
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(medicinaServlet.class.getName()).log(Level.SEVERE, null, ex);
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
