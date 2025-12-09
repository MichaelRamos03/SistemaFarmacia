package com.ues.edu.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jdk.jfr.Enabled;
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
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private int id;
    @Column(name = "Usuario" ,nullable = false,length = 20)
    private String usuario;
    @Column(name = "contrasenia" ,nullable = false)
    private String contrasenia;
    @Column(name = "estado" ,nullable = false)
    private boolean estado;
    
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Persona persona;

     @ManyToOne()
    @JoinColumn(name = "idRol")
    private Rol rol;
    
    public Usuario() {
    }

    public Usuario(int id) {
        this.id = id;
    }


      public Usuario( String usuario, String contrasenia, boolean estado, Rol rol) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.estado = estado;
        this.rol = rol;
    }
    
    public Usuario(int id, String usuario, String contrasenia, boolean estado, Rol rol) {
        this.id = id;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.estado = estado;
        this.rol = rol;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    
   
   
    
}
