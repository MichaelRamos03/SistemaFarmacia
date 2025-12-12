/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package test;

import com.ues.edu.entities.Categoria;
import com.ues.edu.entities.Medicamento;
import com.ues.edu.entities.Persona;
import com.ues.edu.entities.Rol;
import com.ues.edu.entities.Usuario;
import com.ues.edu.entities.Venta;
import com.ues.edu.entities.detalleVenta;
import com.ues.edu.utilidades.Encriptar;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Gaby Laínez
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager enti = emfactory.createEntityManager();
        enti.getTransaction().begin();

        Date fecha1 = new Date("2000/3/15");
        Date fecha2 = new Date("2025/7/22");
        
        Rol r1 = new Rol();
        r1.setNombreRol("Administrador");

        Persona p1 = new Persona();
        p1.setNombrePersona("Admin");
        p1.setFechaNacimiento(fecha1);
        p1.setEdad(25);
        p1.setDui(12345678);
        p1.setTelefono("23541276");
      
        

        Usuario usu1 = new Usuario();
  
        usu1.setUsuario("Admin");
        
        String password = "123admin";
        String passwordIncriptada = Encriptar.getStringMessageDigest(password,Encriptar.SHA256);
        
        usu1.setContrasenia( passwordIncriptada);
        usu1.setEstado(true);
        usu1.setPersona(p1);
        p1.setUsuario(usu1);
        usu1.setRol(r1);

        Medicamento me1 = new Medicamento();
        
        me1.setNombre("Acetaminofen");
        me1.setCantidadExistencias(30);
        me1.setPrecioUnidad(0.20);
        me1.setPrecioTotal(6);
        me1.setFechaIngreso(new Date());
        me1.setFechaDeExpiracion(new Date());
        me1.setDescripcion("alivian dolores de cabeza");
        me1.setActivo(true);
        

        Categoria ca1 = new Categoria();

        ca1.setNombreCategoria("Tabletas");
        ca1.setDescripcion("pastillas via oral");
        List<Medicamento> lista = new ArrayList<>();
        lista.add(me1);
        ca1.setMedicamento(lista);
        ca1.setEstado(true);
       
        
        me1.setCategoria(ca1);
       
         Venta ve11 = new Venta();
        ve11.setFechaVenta(fecha2);
        ve11.setPersona(p1);

        detalleVenta v1 = new detalleVenta();
        v1.setCantidadProducto(23);
        v1.setTotalVendido(24);
        v1.setMedicamento(me1);
        v1.setVenta(ve11);

       
        enti.persist(r1);
        enti.persist(p1);
        enti.persist(usu1);
        enti.persist(me1);
        enti.persist(ca1);
        enti.persist(ve11);
        enti.persist(v1);
        
        
        enti.getTransaction().commit();
        enti.close();
        emfactory.close();

    }

}






