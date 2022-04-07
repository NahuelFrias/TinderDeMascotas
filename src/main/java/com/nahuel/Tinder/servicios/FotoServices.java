package com.nahuel.Tinder.servicios;

import com.nahuel.Tinder.entidades.Foto;
import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.repositorios.FotoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FotoServices {
    
    @Autowired
    FotoRepositorio fotoRepositorio;
    
    // este metodo retorna la foto creada y persistida
    @Transactional /** si el metodo se ejecuta se hace un commit a la b.d. y se aplican los cambios
     * si salta alguna excepcion, se vuelve atras y no se aplican cambios a la b.d
     */
    public Foto guardar (MultipartFile archivo) throws ErrorServicio{// MultipartFile es el archivo donde se guarda la foto
        
        if( archivo != null && archivo.isEmpty()){
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType()); // seteo el tipo mime
                foto.setNombre(archivo.getName());
                foto.setContenide(archivo.getBytes());
                
                return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage()); // si se lanza una excepcion se muestra en la consola
            }
        }
        return null;
    }
    
    @Transactional
    public Foto actualizar (String idFoto, MultipartFile archivo) throws ErrorServicio{
       
         if( archivo != null){
            try {
                Foto foto = new Foto();
                
                if(idFoto != null){
                    Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
                    if(respuesta.isPresent()){
                        foto = respuesta.get();
                    }
                }
                foto.setMime(archivo.getContentType()); // seteo el tipo mime
                foto.setNombre(archivo.getName());
                foto.setContenide(archivo.getBytes());
                
                return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage()); // si se lanza una excepcion se muestra en la consola
            }
        }
        return null;
    }
}
