package com.nahuel.Tinder.servicios;

import com.nahuel.Tinder.entidades.Foto;
import com.nahuel.Tinder.entidades.Mascota;
import com.nahuel.Tinder.entidades.Usuario;
import com.nahuel.Tinder.enumeraciones.Sexo;
import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.repositorios.MascotaRepositorio;
import com.nahuel.Tinder.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MascotaServicio {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private MascotaRepositorio mascotaRepositorio;
    @Autowired
    private FotoServices fotoServicio;
    
    @Transactional /** si el metodo se ejecuta se hace un commit a la b.d. y se aplican los cambios
     * si salta alguna excepcion, se vuelve atras y no se aplican cambios a la b.d
     */
    public void agregarMascota(MultipartFile archivo, String idUsuario, String nombre, Sexo sexo) throws ErrorServicio{
        
        Usuario usuario = usuarioRepositorio.findById(idUsuario).get();
        
        validar(nombre, sexo);
        
        Mascota mascota = new Mascota();
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
        mascota.setAlta(new Date());
        
        Foto foto = fotoServicio.guardar(archivo);
        mascota.setFoto(foto);
        
        mascotaRepositorio.save(mascota);
    }
    
    @Transactional
    public void modificar(MultipartFile archivo, String idUsuario, String idMascota, String nombre, Sexo sexo) throws ErrorServicio{
        
        validar(nombre, sexo);
        
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if(respuesta.isPresent()){
            Mascota mascota = respuesta.get();
            if(mascota.getUsuario().getId().equals(idUsuario)){
                mascota.setNombre(nombre);
                mascota.setSexo(sexo);
                
                String idFoto = null;
                if(mascota.getFoto() != null){
                    idFoto = mascota.getFoto().getId();
                }
                Foto foto = fotoServicio.actualizar(idFoto, archivo);
                mascota.setFoto(foto);
                
                mascotaRepositorio.save(mascota);
            } else {
                throw new ErrorServicio("No tiene permisos suficientes");
            }
        } else {
            throw new ErrorServicio("No existe la mascota solicitada!");
        }
    }
    
    @Transactional
    public void eliminar(String idUsuario, String idMascota) throws ErrorServicio{
        
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if(respuesta.isPresent()){
            Mascota mascota = respuesta.get();
            if(mascota.getUsuario().getId().equals(idUsuario)){
                mascota.setBaja(new Date());
                
                mascotaRepositorio.save(mascota);
            } else {
                throw new ErrorServicio("No tiene permisos suficientes");
            }
        } else {
            throw new ErrorServicio("No existe la mascota solicitada!");
        }
    }
    
    private void validar(String nombre, Sexo sexo) throws ErrorServicio{
        
        if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre no puede estar vacio!");
        }
        if(sexo == null){
            throw new ErrorServicio("El sexo no puede estar vacio!");
        }
        
    }
    
}
