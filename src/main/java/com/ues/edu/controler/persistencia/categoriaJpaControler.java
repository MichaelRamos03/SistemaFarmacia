package com.ues.edu.controler.persistencia;

import com.ues.edu.entities.Categoria;
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
public class categoriaJpaControler {

    
      private final EntityManagerFactory emf = null;
    private final EntityManager em = null;

    private JpaControler<Categoria> jpaController;

    public categoriaJpaControler() {
       
        jpaController = new JpaControler<>(Categoria.class);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

 
    public String create(Categoria usuario) {
        return jpaController.create(usuario);
    }

    public String edit(Categoria usuario) throws NonexistentEntityException, Exception {
        return jpaController.edit(usuario);
    }

  public String destroy(int id) throws NonexistentEntityException {
    try {
        Categoria categoria = jpaController.find(id);
        if (categoria != null) {
            categoria.setEstado(false); // solo desactiva
            return jpaController.edit(categoria); // guarda el cambio
        } else {
            return "no_existe";
        }
    } catch (Exception ex) {
        Logger.getLogger(JpaControler.class.getName()).log(Level.SEVERE, null, ex);
        return "error";
    }
}
public List<Categoria> findCategoriaActivas() {
    return jpaController.findAll().stream()
        .filter(c -> c.isEstado()) 
        .toList();
}

public String reactivar(int id) {
    try {
        Categoria categoria = findCategoria(id);
        if (categoria != null) {
            categoria.setEstado(true);
            edit(categoria); 
            return "exito";
        } else {
            return "no_encontrado";
        }
    } catch (Exception e) {
        e.printStackTrace();
        return "error";
    }
}


public List<Categoria> findCategoriaInactivas() {
    return jpaController.findAll().stream()
        .filter(c -> !c.isEstado()) 
        .toList();
}


    //busca por id
    public Categoria findCategoria(int id) {
        return jpaController.find(id);
    }

    public List<Categoria> findCategoriaEntities() {
        return jpaController.findAll();
    }

    
    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return jpaController.findEntities(maxResults, firstResult);
    }

    
    public int getCategoriaCount() {
        return jpaController.getEntityCount();
    }
    
}
