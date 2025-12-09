/*
 * Servlet Persona - Corregido error de JSON duplicado y Diseño de Tabla
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

@WebServlet(name = "PersonaServlet", urlPatterns = {"/PersonaServlet"})
public class PersonaServlet extends HttpServlet {

    // Eliminamos json y array de aquí para evitar conflictos de memoria

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String op = request.getParameter("opcion");
        
        // CORRECCIÓN CLAVE: Inicializar aquí para que estén limpios en cada petición
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        switch (op) {
            case "cargarCombos": {
                String comboUsuario = "";
                UsuarioJpaControler usuarioJpaControl = new UsuarioJpaControler();
                List<Usuario> usuariList = usuarioJpaControl.findUsuarioEntities();

                if (usuariList != null && !usuariList.isEmpty()) {
                    for (Usuario u : usuariList) {
                        // Solo mostramos usuarios activos (opcional)
                        if(u.isEstado()) {
                            comboUsuario += "<option value='" + u.getId() + "'>" + u.getUsuario() + "</option>";
                        }
                    }
                    json.put("usuario", comboUsuario);
                    json.put("resultado", "exito");
                } else {
                    json.put("resultado", "error");
                }
                array.put(json);
                response.getWriter().write(array.toString());
            }
            break;

            case "insertar": {
                try {
                    String nombre = request.getParameter("nombre");
                    Date fechaNacimiento = Date.valueOf(request.getParameter("fechaNacimiento"));
                    
                    // Calculo de edad
                    LocalDate nacimiento = fechaNacimiento.toLocalDate();
                    LocalDate hoy = LocalDate.now();
                    int edad = Period.between(nacimiento, hoy).getYears();

                    int dui = Integer.parseInt(request.getParameter("dui"));
                    String telefono = request.getParameter("telefono");
                    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                    
                    Usuario usuario = new Usuario(idUsuario);
                    Persona objPersona = new Persona(nombre, fechaNacimiento, edad, dui, telefono, usuario);

                    PersonaJpaControler personaJpaControl = new PersonaJpaControler();
                    String resultado = personaJpaControl.create(objPersona);
                    
                    json.put("resultado", resultado);
                } catch (Exception e) {
                    json.put("resultado", "error_exception");
                    e.printStackTrace();
                }
                array.put(json);
                response.getWriter().write(array.toString());
            }
            break;

            case "si_actualizalo": {
                try {
                    int idPersona = Integer.parseInt(request.getParameter("idPersona"));
                    String nombre = request.getParameter("nombre");
                    Date fechaNacimiento = Date.valueOf(request.getParameter("fechaNacimiento"));
                    
                    LocalDate nacimiento = fechaNacimiento.toLocalDate();
                    LocalDate hoy = LocalDate.now();
                    int edad = Period.between(nacimiento, hoy).getYears();

                    int dui = Integer.parseInt(request.getParameter("dui"));
                    String telefono = request.getParameter("telefono");
                    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                    
                    Usuario usuario = new Usuario(idUsuario);
                    Persona objPersona = new Persona(idPersona, nombre, fechaNacimiento, edad, dui, telefono, usuario);

                    PersonaJpaControler personaJpaControl = new PersonaJpaControler();
                    String actualizar = personaJpaControl.edit(objPersona);
                    
                    json.put("resultado", actualizar.equals("exito") ? "exito" : "error");
                } catch (Exception ex) {
                    json.put("resultado", "error");
                    ex.printStackTrace();
                }
                array.put(json);
                response.getWriter().write(array.toString());
            }
            break;

            case "consultar": {
                PersonaJpaControler personaJpaControl = new PersonaJpaControler();
                
                // Diseño de Tabla alineado con Ventas/Usuarios
                String html = "<table id=\"tabla_persona\" class=\"table table-hover table-bordered nowrap align-middle\" style=\"width:100%\">\n"
                        + "  <thead class=\"bg-light\">\n"
                        + "    <tr>\n"
                        + "      <th scope=\"col\">#</th>\n"
                        + "      <th scope=\"col\">Nombre Empleado</th>\n"
                        + "      <th scope=\"col\">Usuario Sistema</th>\n"
                        + "      <th scope=\"col\">Edad</th>\n"
                        + "      <th scope=\"col\">DUI</th>\n"
                        + "      <th scope=\"col\">Teléfono</th>\n"
                        + "      <th scope=\"col\" class=\"text-center\">Acciones</th>\n"
                        + "    </tr>\n"
                        + "  </thead>\n"
                        + "  <tbody>";

                List<Persona> personaList = personaJpaControl.findPersonaEntities();
                int i = 1;

                if (personaList != null) {
                    for (Persona obj : personaList) {
                        
                        String nombre = (obj.getNombrePersona() != null) ? obj.getNombrePersona() : "---";
                        String usuarioNom = (obj.getUsuario() != null) ? obj.getUsuario().getUsuario() : "Sin Asignar";
                        String inicial = (nombre.length() > 0) ? nombre.substring(0, 1).toUpperCase() : "?";

                        html += "  <tr>\n"
                                + "      <td class='fw-bold text-center'>" + i + "</td>\n"
                                + "      <td>\n"
                                + "        <div class='d-flex align-items-center'>\n"
                                + "           <div class='rounded-circle bg-purple-light text-purple d-flex justify-content-center align-items-center me-2' style='width:35px; height:35px; font-weight:bold;'>" + inicial + "</div>\n"
                                + "           <div>" + nombre + "</div>\n"
                                + "        </div>\n"
                                + "      </td>\n"
                                + "      <td><span class='badge bg-light text-dark border'>" + usuarioNom + "</span></td>\n"
                                + "      <td>" + obj.getEdad() + " Años</td>\n"
                                + "      <td>" + obj.getDui() + "</td>\n"
                                + "      <td>" + obj.getTelefono() + "</td>\n"
                                + "      <td class='text-center'>\n"
                                + "        <div class='d-flex justify-content-center gap-2'>\n"
                                + "          <button class='btn btn-sm btn-outline-purple btn_editar' data-id='" + obj.getId() + "' title='Editar'>\n"
                                + "             <i class='bi bi-pencil-fill'></i>\n"
                                + "          </button>\n"
                                + "          <button class='btn btn-sm btn-outline-danger btn_eliminar' data-id='" + obj.getId() + "' title='Eliminar'>\n"
                                + "             <i class='bi bi-trash-fill'></i>\n"
                                + "          </button>\n"
                                + "        </div>\n"
                                + "      </td>\n"
                                + "  </tr>";
                        i++;
                    }
                }

                html += "  </tbody></table>";

                json.put("resultado", "exito");
                json.put("tabla", html);
                json.put("cantidad", (personaList != null) ? personaList.size() : 0);
                
                array.put(json);
                response.getWriter().write(array.toString());
            }
            break;

            case "editar_consultar": {
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    PersonaJpaControler personaModel = new PersonaJpaControler();
                    Persona p = personaModel.findPersona(id);
                    
                    if (p != null) {
                        JSONObject obj = new JSONObject();
                        obj.put("idPersona", p.getId());
                        obj.put("nombre", p.getNombrePersona());
                        // Convertir fecha a String para input date
                        obj.put("fechaNacimiento", p.getFechaNacimiento().toString()); 
                        obj.put("dui", p.getDui());
                        obj.put("telefono", p.getTelefono());
                        obj.put("idUsuario", (p.getUsuario() != null) ? p.getUsuario().getId() : "");

                        json.put("resultado", "exito");
                        json.put("persona", obj);
                    } else {
                        json.put("resultado", "error");
                    }
                } catch (Exception e) {
                    json.put("resultado", "error");
                }
                array.put(json);
                response.getWriter().write(array.toString());
            }
            break;

            case "eliminar": {
                try {
                    int idElim = Integer.parseInt(request.getParameter("id"));
                    PersonaJpaControler personaModel = new PersonaJpaControler();
                    String resultado = personaModel.destroy(idElim);
                    json.put("resultado", resultado.equals("exito") ? "exito" : "error");
                } catch (Exception ex) {
                    json.put("resultado", "error");
                }
                array.put(json);
                response.getWriter().write(array.toString());
            }
            break;
        }
    }
}