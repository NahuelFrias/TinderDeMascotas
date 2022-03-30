package com.nahuel.Tinder.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Voto implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2") 
    private String id;
    
    @ManyToOne
    private Mascota macota1; // mascote que origina el voto
    
    @ManyToOne
    private Mascota mascota2; // mascota que recibe el voto
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Temporal(TemporalType.TIMESTAMP)
    private Date respuesta; //respuesta del voto
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Date respuesta) {
        this.respuesta = respuesta;
    }

    public Mascota getMacota1() {
        return macota1;
    }

    public void setMacota1(Mascota macota1) {
        this.macota1 = macota1;
    }

    public Mascota getMascota2() {
        return mascota2;
    }

    public void setMascota2(Mascota mascota2) {
        this.mascota2 = mascota2;
    }
    
}
