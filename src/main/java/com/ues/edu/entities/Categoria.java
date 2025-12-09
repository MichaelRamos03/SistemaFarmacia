package com.ues.edu.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
//@AllArgsConstructor
//@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCategoria")
    private Integer idCategoria;
    @Column(name="nombre",nullable = false, length = 40)
    private String nombreCategoria;
    @Column(name="descripcion",nullable = false, length = 100)
    private String descripcion;
    @Column(name ="estado")
    private boolean estado;
    
     
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medicamento> medicamento;

    public Categoria() {
    }

    public Categoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    
    
    public Categoria(Integer idCategoria, String nombreCategoria, String descripcion, boolean estado) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
        this.estado = estado;
    }
    
    

    public Categoria(String nombreCategoria, String descripcion, boolean estado) {
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    
    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Medicamento> getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(List<Medicamento> medicamento) {
        this.medicamento = medicamento;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
     
     
    
}
