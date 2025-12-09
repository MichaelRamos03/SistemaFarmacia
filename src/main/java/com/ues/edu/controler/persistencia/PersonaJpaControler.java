package com.ues.edu.controler.persistencia;

import com.ues.edu.entities.Persona;
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
public class PersonaJpaControler {
  private final EntityManagerFactory emf = null;
    private final EntityManager em = null;

    private JpaControler<Persona> jpaController;

    public PersonaJpaControler() {
        jpaController = new JpaControler<>( Persona.class);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(Persona persona) {
        return jpaController.create(persona);
    }

    public String edit(Persona persona) throws NonexistentEntityException, Exception {
        return jpaController.edit(persona);
    }

    public String destroy(int id) throws NonexistentEntityException {
        String var = "hola";
        try {
            return jpaController.destroy(id);
        } catch (Exception ex) {
            Logger.getLogger(PersonaJpaControler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return var;
    }

    public Persona findPersona(int id) {
        return jpaController.find(id);
    }

    public List<Persona> findPersonaEntities() {
        return jpaController.findAll();
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return jpaController.findEntities(maxResults, firstResult);
    }

    public int getPersonaCount() {
        return jpaController.getEntityCount();
    }
}
