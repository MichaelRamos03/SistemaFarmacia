/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controller;

import com.ues.edu.controler.persistencia.UsuarioJpaControler;
import com.ues.edu.entities.Usuario;
import com.ues.edu.utilidades.Encriptar;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Gaby Laínez
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    
        private final UsuarioJpaControler usuarioController = new UsuarioJpaControler();


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
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
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

            response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        
        String nombreUsuario = request.getParameter("usuario");
        String contrasenia = request.getParameter("contrasenia");

        
        String contraseniaEncriptada = Encriptar.getStringMessageDigest(contrasenia, Encriptar.SHA256);

        
        Usuario usuario = new Usuario();
        usuario.setUsuario(nombreUsuario);
        usuario.setContrasenia(contraseniaEncriptada);
        
        System.out.println("Usuario ingresado: " + nombreUsuario);
        System.out.println("Contraseña ingresada: " + contrasenia);
        System.out.println("Contraseña encriptada: " + contraseniaEncriptada);

        
        Usuario usuarioValido = usuarioController.validarUsuario(usuario);

        
        String jsonResponse;

        if (usuarioValido != null) {
           
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioValido);

            jsonResponse = "{\"success\": true, \"nombre\": \"" + usuarioValido.getUsuario() + "\"}";
        } else {
            jsonResponse = "{\"success\": false}";
        }

        out.print(jsonResponse);
        out.flush();
        
      
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
