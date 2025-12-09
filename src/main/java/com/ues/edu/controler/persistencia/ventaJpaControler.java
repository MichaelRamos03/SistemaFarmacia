package com.ues.edu.controler.persistencia;

import com.ues.edu.entities.Venta;
import com.ues.edu.persistencia.exception.NonexistentEntityException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gaby Laínez
 */
public class ventaJpaControler {

    private final EntityManagerFactory emf = null;
    private final EntityManager em = null;

    private JpaControler<Venta> jpaController;

    public ventaJpaControler() {
        jpaController = new JpaControler<>( Venta.class);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(Venta venta) {
        
        return jpaController.create(venta);
    }

    public String edit(Venta venta) throws NonexistentEntityException, Exception {
        return jpaController.edit(venta);
    }

  
    public String destroy(int id) throws NonexistentEntityException {
        String var = "hola";
        try {
            return jpaController.destroy(id);
        } catch (Exception ex) {
            Logger.getLogger(ventaJpaControler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return var;
    }

    public Venta findVenta(int id) {
        return jpaController.find(id);
    }

    public List<Venta> findVentaEntities() {
        return jpaController.findAll();
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return jpaController.findEntities(maxResults, firstResult);
    }

    public int getVentaCount() {
        return jpaController.getEntityCount();
    }
    
    
}
