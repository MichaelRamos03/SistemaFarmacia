/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controller;

import com.ues.edu.controler.persistencia.categoriaJpaControler;
import com.ues.edu.entities.Categoria;
import com.ues.edu.persistencia.exception.NonexistentEntityException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Gaby Laínez
 */
@WebServlet(name = "categoriaServelet", urlPatterns = {"/categoriaServelet"})
public class categoriaServelet extends HttpServlet {

    private List<Categoria> categoriaList;
    private Categoria categoria;
    private Categoria categoriaRecuperada;

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
            out.println("<title>Servlet categoriaServelet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet categoriaServelet at " + request.getContextPath() + "</h1>");
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

            case "insertar": // Cuando también modifica.
            {
                try {
                    String nombre = request.getParameter("nombreCategoria");
                    String descripcion = request.getParameter("descripcion");
                    String estadoStr = request.getParameter("estado");
                    boolean estado = "activo".equalsIgnoreCase(estadoStr);

                    Categoria objCategoria = new Categoria(nombre, descripcion, estado);

                    categoriaJpaControler categoriaJpaControl = new categoriaJpaControler(); // emf

                    System.out.println("nombre " + nombre);
                    System.out.println("descripcion  " + descripcion);

                    String resultado = categoriaJpaControl.create(objCategoria);
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
                    int id = Integer.parseInt(request.getParameter("idCategoria"));
                    String nombre = request.getParameter("nombreCategoria");

                    String descripcion = request.getParameter("descripcion");
                    String estadoStr = request.getParameter("estado");
                    boolean estado = "activo".equalsIgnoreCase(estadoStr);

                    Categoria objCategoria = new Categoria(id, nombre, descripcion, estado);

                    categoriaJpaControler categoriaJpaControl = new categoriaJpaControler(); // emf

                    String actualizar = categoriaJpaControl.edit(objCategoria);
                    if (actualizar.equals("exito")) {
                        this.json.put("resultado", "exito");
                    } else {
                        this.json.put("resultado", "error");
                    }
                    this.array.put(this.json);
                    response.getWriter().write(this.array.toString());
                } catch (Exception ex) {
                    Logger.getLogger(categoriaServelet.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }

            break;

            case "consultar": {
                categoriaJpaControler categoriaJpaControl = new categoriaJpaControler(); //emf
                
                String html = "<table class=\"table\" id=\"tabla_categoria\""
                        + "class=\"table table-bordered dt-responsive nowrap\" width=\"100%\">\n"
                        + "  <thead>\n"
                        + "    <tr>\n"
                        + "      <th scope=\"col\">#</th>\n"
                        + "      <th scope=\"col\">Nombre Categoria;</th>\n"
                        + "      <th scope=\"col\">Descripcion:</th>\n"
                        + "      <th scope=\"col\">estado:</th>\n"
                        + "      <th scope=\"col\">Acciones</th>\n"
                        + "    </tr>\n"
                        + "  </thead>\n"
                        + "  <tbody>";
                this.categoriaList = categoriaJpaControl.findCategoriaActivas(); 
                int cont = 0;

                int i = 1;
                for (Categoria obj : this.categoriaList) {

                    cont++;
                    html += "  <tr>\n"
                            + "      <td>" + i + "</td>\n"
                            + "      <td>" + obj.getNombreCategoria() + "</td>\n"
                            + "      <td>" + obj.getDescripcion() + "</td>\n"
                            + "      <td>" + (obj.isEstado() ? "Activo" : "Inactivo") + "</td>\n"
                            + "<td>"
                            + "<div class='dropdown m-b-10'>"
                            + "<button class='btn btn-secondary dropdown-toggle'"
                            + " type='button' id='dropdownMenuButton' data-toggle='dropdown'  aria-haspopup='true'"
                            + "aria-expanded='false'> Seleccione</button>"
                            + "<div class='dropdown-menu' aria-labelledby='dropdownMenuButton'>"
                            + (obj.isEstado()
                            ? "<a class='dropdown-item btn_eliminar' data-id='" + obj.getIdCategoria() + "' href='javascript:void(0)'>Eliminar</a>"
                            : "<a class='dropdown-item btn_activar' data-id='" + obj.getIdCategoria() + "' href='javascript:void(0)'>Activar</a>")
                            + "<a class='dropdown-item btn_editar' data-id='" + obj.getIdCategoria() + "' href='javascript:void(0)'>Actualizar</a>"
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
                JSONObject CategoriaJsonObject = new JSONObject();
                categoriaJpaControler CategoriaModel = new categoriaJpaControler(); //emf
                int id = Integer.parseInt(request.getParameter("idCategoria"));

                this.categoria = new Categoria(); // Es indispensable antes de setear sino da error

                this.categoria.setIdCategoria(id);

                this.categoriaRecuperada = new Categoria();
                this.categoriaRecuperada = CategoriaModel.findCategoria(id);

                if (this.categoriaRecuperada != null) {
                    CategoriaJsonObject.put("idCategoria", this.categoriaRecuperada.getIdCategoria());
                    CategoriaJsonObject.put("nombreCategoria", this.categoriaRecuperada.getNombreCategoria());
                    CategoriaJsonObject.put("descripcion", this.categoriaRecuperada.getDescripcion());
                    CategoriaJsonObject.put("estado", this.categoriaRecuperada.isEstado() ? "activo" : "inactivo");

                    this.json.put("resultado", "exito");
                    this.json.put("categoria", CategoriaJsonObject);
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
                    categoriaJpaControler CategoriaModel = null;
                    CategoriaModel = new categoriaJpaControler(); //emf
                    int idElim = Integer.parseInt(request.getParameter("idCategoria"));
                    resultado = CategoriaModel.destroy(idElim);
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
                categoriaJpaControler CategoriaModel = new categoriaJpaControler();
                List<Categoria> lista = CategoriaModel.findCategoriaInactivas();

                JSONArray listaJson = new JSONArray();
                for (Categoria c : lista) {
                    JSONObject obj = new JSONObject();
                    obj.put("idCategoria", c.getIdCategoria());
                    obj.put("nombre", c.getNombreCategoria());
                    obj.put("descripcion", c.getDescripcion());
                    listaJson.put(obj);
                }

                response.setContentType("application/json");
                response.getWriter().write(listaJson.toString());

            }
            break;
            case "reactivar": {
                categoriaJpaControler categoriaControl = new categoriaJpaControler();
                int id = Integer.parseInt(request.getParameter("idCategoria"));

                String resultado = categoriaControl.reactivar(id); // ✅ CORREGIDO
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
