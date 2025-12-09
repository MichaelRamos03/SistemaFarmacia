package com.ues.edu.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
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
@Table(name = "persona")
public class Persona {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "idPersona")
    private Integer id;
    @Column(name = "nombre" ,nullable = false,length = 50)
    private String nombrePersona;
    @Column(name = "fechaNacimiento" ,nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "edad",nullable = false)
    private int edad;
    @Column(name = "dui" ,nullable = false,length = 8)
    private Integer dui;
    @Column(name = "telefono" ,nullable = false,length = 9)
    private String telefono;
    
    @OneToOne
    @JoinColumn(name = "idUsuario", unique = true)
    private Usuario usuario;
    

    
     @OneToMany(mappedBy = "persona",cascade = CascadeType.ALL,orphanRemoval = true)
     private List<Venta>venta;

    public Persona() {
    }

    public Persona(Integer id) {
        this.id = id;
    }

    
    
    public Persona(String nombrePersona, Date fechaNacimiento, int edad, Integer dui, String telefono, Usuario usuario) {
        this.nombrePersona = nombrePersona;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.dui = dui;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    public Persona(Integer id, String nombrePersona, Date fechaNacimiento, int edad, Integer dui, String telefono, Usuario usuario) {
        this.id = id;
        this.nombrePersona = nombrePersona;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.dui = dui;
        this.telefono = telefono;
        this.usuario = usuario;
    }

 

    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    } 

    public Integer getDui() {
        return dui;
    }

    public void setDui(Integer dui) {
        this.dui = dui;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }



    public List<Venta> getVenta() {
        return venta;
    }

    public void setVenta(List<Venta> venta) {
        this.venta = venta;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
     
     

}
