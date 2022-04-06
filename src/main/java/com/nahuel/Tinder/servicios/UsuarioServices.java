package com.nahuel.Tinder.servicios;

import com.nahuel.Tinder.entidades.Foto;
import com.nahuel.Tinder.entidades.Usuario;
import com.nahuel.Tinder.entidades.Zona;
import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.repositorios.UsuarioRepositorio;
import com.nahuel.Tinder.repositorios.ZonaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServices implements UserDetailsService { // autentizar usuarios mediante la seguridad de Spring

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private FotoServices fotoServicio;
    @Autowired
    private NotificacionServices notificaciónServicio;
    @Autowired
    private ZonaRepositorio zonaRepositorio;

    @Transactional /** si el metodo se ejecuta se hace un commit a la b.d. y se aplican los cambios
     * si salta alguna excepcion, se vuelve atras y no se aplican cambios a la b.d
     */
    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, String clave1, String clave2, String idZona) throws ErrorServicio {

        Zona zona = zonaRepositorio.getById(idZona);
        
        validar(nombre, apellido, mail, clave1, clave2, zona);
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setZona(zona);

        // debo utilizar el mismo encriptador que utilizo con Spring Security en la app principal
        // cuando persisto al usuario lo hago con una clave encriptada
        String encriptada = new BCryptPasswordEncoder().encode(clave1);
        usuario.setClave(encriptada);
        usuario.setAlta(new Date());

        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);

        usuarioRepositorio.save(usuario);
        
        // damos la bienvenida por Mail
        //notificaciónServicio.enviar("Bienvenido al Tinder de Mascotas!", "Tinder de Mascotas", usuario.getMail());
    }

    // necesito el id para buscar en la base de datos al usuario
    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String mail, String clave1, String clave2, String idZona) throws ErrorServicio {

        Zona zona = zonaRepositorio.getById(idZona);
        
        validar(nombre, apellido, mail, clave1, clave2, zona);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setZona(zona);
            // encripto la clave
            String encriptada = new BCryptPasswordEncoder().encode(clave1);
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
    
    public Usuario buscarPorId(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario= respuesta.get();
            return usuario;
        } else {
            throw new ErrorServicio("No se encontro el usuario buscado.");
        }
    }

    @Transactional
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

    @Transactional
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

    private void validar(String nombre, String apellido, String mail, String clave1, String clave2, Zona zona) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio!");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede estar vacio!");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede estar vacio!");
        }
        if (clave1 == null || clave1.isEmpty() || clave1.length() <= 6) {
            throw new ErrorServicio("La clave debe tener al menos 6 digitos!");
        }
        if(!clave1.equals(clave2)){
            throw new ErrorServicio("Las claves no coinciden!");
        }
        if(zona == null){
            throw new ErrorServicio("No se encontro la zona solicitada");
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
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);
            
            // recupero los atributos de la solicitud o request http
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true); // solicito los datos de sesion de esa solicitud http
            session.setAttribute("usuariosession", usuario); // guardo un usuario session en los datos de solicitud http, tengo mi objeto con los datos de mi usuario
            
            //transformo al cliente en un cliente del dominio de Spring
            //nos pide usuario,clave y permisos
            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else { //si el usuario no existe retorno null
            return null;
        }
    }

}
