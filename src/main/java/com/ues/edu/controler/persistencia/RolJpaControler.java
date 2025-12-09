package com.ues.edu.controler.persistencia;

import com.ues.edu.entities.Rol;
import com.ues.edu.entities.Usuario;
import com.ues.edu.persistencia.exception.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gaby Laínez
 */
public class RolJpaControler {

     private JpaControler<Rol> jpaController;

    public RolJpaControler() {
        jpaController = new JpaControler<>(Rol.class);
    }

    public String create(Rol rol) {
        if (rol.getUsuario() == null) {
            rol.setUsuario(new ArrayList<Usuario>());
        }
        return jpaController.create(rol);
    }

    public String edit(Rol rol) throws NonexistentEntityException, Exception {
        return jpaController.edit(rol);
    }

    public String destroy(int id) throws NonexistentEntityException {
        String var = "hola";
        try {
            return jpaController.destroy(id);
        } catch (Exception ex) {
            Logger.getLogger(RolJpaControler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return var;
    }

    public Rol findRol(int id) {
        return jpaController.find(id);
    }

    public List<Rol> findRolEntities() {
        return jpaController.findAll();
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return jpaController.findEntities(maxResults, firstResult);
    }

    public int getRolCount() {
        return jpaController.getEntityCount();
    }
    
}
