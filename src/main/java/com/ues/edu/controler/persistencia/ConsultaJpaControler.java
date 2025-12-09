package com.ues.edu.controler.persistencia;

import com.ues.edu.controler.persistencia.JpaControler;
import com.ues.edu.entities.Medicamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

/**
 *
 * @author Gaby Laínez
 */
public class ConsultaJpaControler {

    private final EntityManagerFactory emf = null;
    private final EntityManager em = null;

    private JpaControler<Medicamento> jpaController;

    public ConsultaJpaControler() {
     
        jpaController = new JpaControler<>(Medicamento.class);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Medicamento> findMedicamentoEntities() {
        
       
        return jpaController.findAll();
    }

    public List<Medicamento> findReservaEntities(int maxResults, int firstResult) {
        return jpaController.findEntities(maxResults, firstResult);
    }
}


