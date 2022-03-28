package com.nahuel.Tinder.servicios;

import com.nahuel.Tinder.entidades.Usuario;
import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServices {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    public void registrar(String nombre, String apellido, String mail, String clave ) throws ErrorServicio{
        
        validar(nombre, apellido, mail, clave);
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setClave(clave);
        usuario.setAlta(new Date());
        
        usuarioRepositorio.save(usuario);
    }
    
    // necesito el id para buscar en la base de datos al usuario
    public void modificar (String id, String nombre, String apellido, String mail, String clave) throws ErrorServicio{
        
        validar(nombre, apellido, mail, clave);
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setClave(clave);
        
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado!");
        }
    }
    
    public void habilitar(String id) throws ErrorServicio{
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setBaja(null);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado!");
        }
        
    }
    
    public void deshabilitar(String id) throws ErrorServicio{
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado!");
        }
        
    }
    
    private void validar(String nombre, String apellido, String mail, String clave) throws ErrorServicio{
        if (nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre no puede estar vacio!");
        }
        if (apellido == null || apellido.isEmpty()){
            throw new ErrorServicio("El apellido no puede estar vacio!");
        }
        if (mail == null || mail.isEmpty()){
            throw new ErrorServicio("El mail no puede estar vacio!");
        }
        if (clave == null || clave.isEmpty() || clave.length() <= 6){
            throw new ErrorServicio("La clave debe tener al menos 6 digitos!");
        }
    }
    
}
