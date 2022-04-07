package com.nahuel.Tinder.controladores;

import com.nahuel.Tinder.entidades.Mascota;
import com.nahuel.Tinder.entidades.Usuario;
import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.servicios.MascotaServices;
import com.nahuel.Tinder.servicios.UsuarioServices;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoController {
    
    @Autowired
    private UsuarioServices usuarioServicio;
    @Autowired
    private MascotaServices mascotaServicio;
    
    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario (@PathVariable String id){ // devuelve la foto de un usuario
        
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            if( usuario.getFoto() == null){ // puede pasar que el usuario no tenga foto
                throw new ErrorServicio("El usuario no tiene una foto asignada!");
            }
            byte[] foto = usuario.getFoto().getContenide();
            
            HttpHeaders headers = new HttpHeaders(); // clase de Spring
            headers.setContentType(MediaType.IMAGE_JPEG); // esta cabecera le dice al navegador que estoy devolviendo una imagen
            
            return new ResponseEntity<>(foto, headers, HttpStatus.OK); // en HttpStatus devolvemos un codigo 200 (se ejecuto bien la peticion)
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // no existe la imagen
        }        
    }
    
    @GetMapping("/mascota/{id}")
    public ResponseEntity<byte[]> fotoMascota (@PathVariable String id){ // devuelve la foto de un usuario
        
        try {
            Mascota mascota = mascotaServicio.buscarPorId(id);
            if( mascota.getFoto() == null){ // puede pasar que el usuario no tenga foto
                throw new ErrorServicio("El usuario no tiene una foto asignada!");
            }
            byte[] foto = mascota.getFoto().getContenide();
            
            HttpHeaders headers = new HttpHeaders(); // clase de Spring
            headers.setContentType(MediaType.IMAGE_JPEG); // esta cabecera le dice al navegador que estoy devolviendo una imagen
            
            return new ResponseEntity<>(foto, headers, HttpStatus.OK); // en HttpStatus devolvemos un codigo 200 (se ejecuto bien la peticion)
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // no existe la imagen
        }
    }
}
