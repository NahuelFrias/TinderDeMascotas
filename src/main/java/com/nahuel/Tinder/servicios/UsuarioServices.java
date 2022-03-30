package com.nahuel.Tinder.servicios;

import com.nahuel.Tinder.entidades.Foto;
import com.nahuel.Tinder.entidades.Usuario;
import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServices implements UserDetailsService { // autentizar usuarios mediante la seguridad de Spring

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private FotoServices fotoServicio;

    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, String clave) throws ErrorServicio {

        validar(nombre, apellido, mail, clave);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);

        // debo utilizar el mismo encriptador que utilizo con Spring Security en la app principal
        // cuando persisto al usuario lo hago con una clave encriptada
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setAlta(new Date());

        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);

        usuarioRepositorio.save(usuario);
    }

    // necesito el id para buscar en la base de datos al usuario
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String mail, String clave) throws ErrorServicio {

        validar(nombre, apellido, mail, clave);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            // encripto la clave
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            // obtengo el id de la foto si es que existe
            String idFoto = null;
            if (usuario.getFoto() != null) {
                idFoto = usuario.getFoto().getId();
            }
            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            usuario.setFoto(foto);

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado!");
        }
    }

    public void habilitar(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(null);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado!");
        }

    }

    public void deshabilitar(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado!");
        }

    }

    private void validar(String nombre, String apellido, String mail, String clave) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio!");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede estar vacio!");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede estar vacio!");
        }
        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio("La clave debe tener al menos 6 digitos!");
        }
    }

    @Override // cambiamos el String por mail

    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        if (usuario != null) {
            // busca el ususario por el mail y lo transforma en un usuario del dominio de Spring Security
            //esta lista contiene el listado de permisos del usuario
            List<GrantedAuthority> permisos = new ArrayList<>();
            //creo los permisos
            //con este rol va a poder ingresar a algunos metodos
            GrantedAuthority p1 = new SimpleGrantedAuthority("MODULO_FOTOS");
            permisos.add(p1);
            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_MASCOTAS");
            permisos.add(p2);
            GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_VOTOS");
            permisos.add(p3);
            //transformo al cliente en un cliente del dominio de Spring
            //nos pide usuario,clave y permisos
            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else { //si el usuario no existe retorno null
            return null;
        }
    }

}
