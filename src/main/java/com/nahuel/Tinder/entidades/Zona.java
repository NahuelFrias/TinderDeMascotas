package com.nahuel.Tinder.entidades;

import javax.persistence.Entity; //@entity para persistir la entidad Zona
import javax.persistence.Id;

@Entity
public class Zona {
    
    @Id //identificador univoco
    private String id;
    private String nombre;
    private String descripcion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
