package com.nahuel.Tinder.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Foto implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String nombre;
    private String mime; //me dice que tipo de foto es (el formato del archivo)
    
    @Lob // identifica que el tipo de dato es pesado, utiliza un tipo de dato especifico para almacenar el contenido en la b.d.
    @Basic(fetch = FetchType.LAZY) // Lazy = carga perezosa. No carga el contenido de la foto automaticamente si no cuando lo pide
                                                             // Esto hace que los querys sean mas livianos
    private byte [] contenide; // guardamos el contenido de la foto como un arreglo de bytes

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

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public byte[] getContenide() {
        return contenide;
    }

    public void setContenide(byte[] contenide) {
        this.contenide = contenide;
    }
    
    
}
