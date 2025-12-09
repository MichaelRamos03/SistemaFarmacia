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
@Table(name = "detalle_venta")
public class detalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalle_venta", nullable = false)
    private Integer idDetalle_venta;
    @Column(name = "cantidadProducto")
    private double cantidadProducto;
    @Column(name = "totalVendido", nullable = false)
    private double totalVendido;

    @ManyToOne
    @JoinColumn(name = "idVenta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "idMedicamento")
    private Medicamento medicamento;

    public detalleVenta() {
    }

    public detalleVenta(double cantidadProducto, double totalVendido, Venta venta, Medicamento medicamento) {
        this.cantidadProducto = cantidadProducto;
        this.totalVendido = totalVendido;
        this.venta = venta;
        this.medicamento = medicamento;
    }

    public detalleVenta(Integer idDetalle_venta, double cantidadProducto, double totalVendido, Venta venta, Medicamento medicamento) {
        this.idDetalle_venta = idDetalle_venta;
        this.cantidadProducto = cantidadProducto;
        this.totalVendido = totalVendido;
        this.venta = venta;
        this.medicamento = medicamento;
    }

    public Integer getIdDetalle_venta() {
        return idDetalle_venta;
    }

    public void setIdDetalle_venta(Integer idDetalle_venta) {
        this.idDetalle_venta = idDetalle_venta;
    }

    public double getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(double cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public double getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(double totalVendido) {
        this.totalVendido = totalVendido;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    
    
}