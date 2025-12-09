package com.ues.edu.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Gaby Laínez
 */

@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRol" ,nullable = false)
    private Integer idRol;
    @Column(name = "nombreRol" ,nullable = false,length = 50)
    private String nombreRol;
    
    @OneToMany(mappedBy = "rol",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Usuario>usuario;
    
    

    public Rol() {
    }

    public Rol(Integer idRol) {
        this.idRol = idRol;
    }
    

    public Rol(Integer idRol, String nombreRol) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    public Rol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    
    
    
    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public List<Usuario> getUsuario() {
        return usuario;
    }

    public void setUsuario(List<Usuario> usuario) {
        this.usuario = usuario;
    }

 
    
    
    
}
