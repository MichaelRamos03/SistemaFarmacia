package com.ues.edu.controler.persistencia;

import com.ues.edu.entities.Usuario;
import com.ues.edu.entities.Usuario;
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
public class UsuarioJpaControler {
    
    
    private final EntityManagerFactory emf = null;
    private final EntityManager em = null;

    private JpaControler<Usuario> jpaController;

    public UsuarioJpaControler() {
        jpaController = new JpaControler<>(Usuario.class);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Usuario validarUsuario(Usuario usuario) {
        EntityManager em = jpaController.getEntityManager(); 
        try {
            return em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.usuario = :usuario AND u.contrasenia = :contrasenia", Usuario.class)
                    .setParameter("usuario", usuario.getUsuario())
                    .setParameter("contrasenia", usuario.getContrasenia())
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

  public String create(Usuario usuario) {
        return jpaController.create(usuario);
    }

    public String edit(Usuario usuario) throws NonexistentEntityException, Exception {
        return jpaController.edit(usuario);
    }

  public String destroy(int id) throws NonexistentEntityException {
    try {
        Usuario categoria = jpaController.find(id);
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
public List<Usuario> findUsuarioActivas() {
    return jpaController.findAll().stream()
        .filter(c -> c.isEstado()) 
        .toList();
}

public String reactivar(int id) {
    try {
        Usuario categoria = findUsuario(id);
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


public List<Usuario> findUsuarioInactivas() {
    return jpaController.findAll().stream()
        .filter(c -> !c.isEstado()) 
        .toList();
}


    public Usuario findUsuario(int id) {
        return jpaController.find(id);
    }

    public List<Usuario> findUsuarioEntities() {
        return jpaController.findAll();
    }

    
    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return jpaController.findEntities(maxResults, firstResult);
    }

    
    public int getUsuarioCount() {
        return jpaController.getEntityCount();
    }

}
