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
@Table(name = "medicamento")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMedicamento")
    private Integer idMedicamento;
    @Column(name = "nombre",nullable = false,length = 40, unique = true)
    private String nombre;
    @Column(name = "cantidadExistencias",nullable = false)
    private double cantidadExistencias;
    @Column(name = "precioUnidad",nullable = false)
    private double precioUnidad;
    @Column(name ="precioTotal")
    private double precioTotal;
    
    @Column(name = "fechaIngreso",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    
    @Column(name = "fechaDeExpiracion",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDeExpiracion;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "activo")
    private boolean activo;
    
    
  @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "medicamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<detalleVenta> detalleVenta;


    public Medicamento() {}

    public Medicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Medicamento(String nombre, double cantidadExistencias, double precioUnidad, double precioTotal, Date fechaIngreso, Date fechaDeExpiracion, String descripcion, boolean activo, Categoria categoria) {
        this.nombre = nombre;
        this.cantidadExistencias = cantidadExistencias;
        this.precioUnidad = precioUnidad;
        this.precioTotal = precioTotal;
        this.fechaIngreso = fechaIngreso;
        this.fechaDeExpiracion = fechaDeExpiracion;
        this.descripcion = descripcion;
        this.activo = activo;
        this.categoria = categoria;
    }

    
//    
//    public Medicamento(String nombre, Integer cantidadExistencias, double precioUnidad, Date fechaIngreso, Date fechaDeExpiracion, String descripcion, boolean activo, Categoria categoria) {
//        this.nombre = nombre;
//        this.cantidadExistencias = cantidadExistencias;
//        this.precioUnidad = precioUnidad;
//        this.fechaIngreso = fechaIngreso;
//        this.fechaDeExpiracion = fechaDeExpiracion;
//        this.descripcion = descripcion;
//        this.activo = activo;
//        this.categoria = categoria;
//    }

    public Medicamento(Integer idMedicamento, String nombre, double cantidadExistencias, double precioUnidad, double precioTotal, Date fechaIngreso, Date fechaDeExpiracion, String descripcion, boolean activo, Categoria categoria) {
        this.idMedicamento = idMedicamento;
        this.nombre = nombre;
        this.cantidadExistencias = cantidadExistencias;
        this.precioUnidad = precioUnidad;
        this.precioTotal = precioTotal;
        this.fechaIngreso = fechaIngreso;
        this.fechaDeExpiracion = fechaDeExpiracion;
        this.descripcion = descripcion;
        this.activo = activo;
        this.categoria = categoria;
    }

    
    

    
    

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCantidadExistencias() {
        return cantidadExistencias;
    }

    public void setCantidadExistencias(double cantidadExistencias) {
        this.cantidadExistencias = cantidadExistencias;
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaDeExpiracion() {
        return fechaDeExpiracion;
    }

    public void setFechaDeExpiracion(Date fechaDeExpiracion) {
        this.fechaDeExpiracion = fechaDeExpiracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<detalleVenta> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(List<detalleVenta> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }
    
    

}
    

