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
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

/**
 *
 * @author Gaby Laínez
 */
@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "Venta")
public class Venta {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "idVenta" ,nullable = false)
     private Integer id;
     @Column(name = "fechaVenta" ,nullable = false)
     private Date fechaVenta;
     
     @ManyToOne
     @JoinColumn(name = "idPersona")
     private Persona persona;
     
   @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<detalleVenta> detalleVentas;

    public Venta() {
    }

    public Venta(Integer id) {
        this.id = id;
    }
    

    public Venta(Date fechaVenta, Persona persona) {
        this.fechaVenta = fechaVenta;
        this.persona = persona;
    }

    
    
    public Venta(Integer id, Date fechaVenta, Persona persona) {
        this.id = id;
        this.fechaVenta = fechaVenta;
        this.persona = persona;
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<detalleVenta> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<detalleVenta> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }
   
   
   
    
}
