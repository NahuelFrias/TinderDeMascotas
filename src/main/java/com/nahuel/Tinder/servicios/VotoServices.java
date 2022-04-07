package com.nahuel.Tinder.servicios;

import com.nahuel.Tinder.entidades.Mascota;
import com.nahuel.Tinder.entidades.Voto;
import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.repositorios.MascotaRepositorio;
import com.nahuel.Tinder.repositorios.VotoRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoServices {
    
    @Autowired
    private VotoRepositorio votoRepositorio;
    @Autowired
    private MascotaRepositorio mascotaRepositorio;

    @Autowired
    private NotificacionServices notificacionServicio;
    
    
    public void votar (String idUsuario, String idMascota1, String idMascota2) throws ErrorServicio{
        
        Voto voto = new Voto();
        voto.setFecha(new Date());
        
        if(idMascota1.equals(idMascota2)){
            throw new ErrorServicio("No puede votarse a sí mismo!");
        }
        
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota1);
        if(respuesta.isPresent()){
            Mascota mascota1 = respuesta.get();
            if(mascota1.getUsuario().getId().equals(idUsuario)){
                voto.setMacota1(mascota1);
            } else {
                throw new ErrorServicio("No tiene permisos suficientes");
            }
        } else {
            throw new ErrorServicio("No se encontró la mascota!");
        }
        
        Optional<Mascota> respuesta2 = mascotaRepositorio.findById(idMascota2);
        if(respuesta2.isPresent()){
            Mascota mascota2 = respuesta2.get();
            voto.setMascota2(mascota2);
            
            // notificamos el voto
            //notificacionServicio.enviar("Tu mascota ha sido votada!", "Tinder de Mascotas", mascota2.getUsuario().getMail());
        } else {
            throw new ErrorServicio("No se encontró la mascota!");
        }
        
        votoRepositorio.save(voto);
    }
    
    public void responder (String idUsuario, String idVoto) throws ErrorServicio{
        
        Optional<Voto> respuesta = votoRepositorio.findById(idVoto);
        if(respuesta.isPresent()){
            Voto voto = respuesta.get();
            if(voto.getMascota2().getUsuario().getId().equals(idUsuario)){
                voto.setRespuesta(new Date());
                // notificamos el voto
                //notificacionServicio.enviar("Tu voto ha sido correspondido!", "Tinder de Mascotas", voto.getMacota1().getUsuario().getMail());
                votoRepositorio.save(voto);
            } else {
                throw new ErrorServicio("No tiene permisos suficientes!");
            }
        } else {
            throw new ErrorServicio("El voto no existe!");
        }
    }
    
}
