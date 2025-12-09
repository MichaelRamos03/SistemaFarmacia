package com.ues.edu.controler.persistencia;

import com.ues.edu.entities.Medicamento;
import com.ues.edu.entities.Medicamento;
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
public class MedicinaJpaControler {
    
     private final EntityManagerFactory emf = null;
    private final EntityManager em = null;

    private JpaControler<Medicamento> jpaController;

    
    public MedicinaJpaControler() {
        jpaController = new JpaControler<>( Medicamento.class);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(Medicamento medicamento) {
        return jpaController.create(medicamento);
    }

    public String edit(Medicamento medicamento) throws NonexistentEntityException, Exception {
        return jpaController.edit(medicamento);
    }

    public String destroy(int id) throws NonexistentEntityException {
        String var = "hola";
        try {
            return jpaController.destroy(id);
        } catch (Exception ex) {
            Logger.getLogger(MedicinaJpaControler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return var;
    }

    public Medicamento findMedicamento(int id) {
        return jpaController.find(id);
    }

    public List<Medicamento> findMedicamentoEntities() {
        return jpaController.findAll();
    }

    public List<Medicamento> findMedicamentoEntities(int maxResults, int firstResult) {
        return jpaController.findEntities(maxResults, firstResult);
    }

    public int getMedicamentoCount() {
        return jpaController.getEntityCount();
    }

}
