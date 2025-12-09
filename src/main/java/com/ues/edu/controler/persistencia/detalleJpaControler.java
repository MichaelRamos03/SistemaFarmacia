package com.ues.edu.controler.persistencia;

import com.ues.edu.entities.detalleVenta;
import com.ues.edu.persistencia.exception.NonexistentEntityException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gaby Laínez
 */
public class detalleJpaControler {

    private final EntityManagerFactory emf = null;
    private final EntityManager em = null;

    private JpaControler<detalleVenta> jpaController;

    public detalleJpaControler() {

        jpaController = new JpaControler<>(detalleVenta.class);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public String create(detalleVenta DetalleVenta) {
        return jpaController.create(DetalleVenta);
    }

    public String edit(detalleVenta DetalleVenta) throws NonexistentEntityException, Exception {
        return jpaController.edit(DetalleVenta);
    }


    public String destroy(int id) throws NonexistentEntityException {
        String var = "hola";
        try {
            return jpaController.destroy(id);
        } catch (Exception ex) {
            Logger.getLogger(detalleJpaControler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return var;
    }

    public detalleVenta finddetalleVenta(int id) {
        return jpaController.find(id);
    }

    public List<detalleVenta> finddetalleVentaEntities() {
        return jpaController.findAll();
    }

    public List<detalleVenta> finddetalleVentaEntities(int maxResults, int firstResult) {
        return jpaController.findEntities(maxResults, firstResult);
    }

    public int getdetalleVentaCount() {
        return jpaController.getEntityCount();
    }
    
    public List<detalleVenta> findDetalleVentaByIdVenta(int idVenta) {
    EntityManager em =  jpaController.getEntityManager();
    try {
        TypedQuery<detalleVenta> query = em.createQuery(
            "SELECT d FROM detalleVenta d WHERE d.venta.id = :idVenta", detalleVenta.class);
        query.setParameter("idVenta", idVenta);
        return query.getResultList();
    } finally {
        em.close();
    }
}


}
