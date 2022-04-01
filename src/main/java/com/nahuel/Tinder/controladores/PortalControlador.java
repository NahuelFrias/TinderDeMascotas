package com.nahuel.Tinder.controladores;

import com.nahuel.Tinder.entidades.Zona;
import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.repositorios.ZonaRepositorio;
import com.nahuel.Tinder.servicios.UsuarioServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller("")
@RequestMapping("/") // url que escucha este controlador
public class PortalControlador {

    @Autowired
    UsuarioServices usuarioServicio;
    @Autowired
    ZonaRepositorio zonaRepositorio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")/** para ingresar a esta url el usuario debe estar autorizado
     * esto esta en UsuarioServicio en el metodo loadUserByUsername (al final)
     */
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, // required false: no siempre puede viajar este parametro "error"
            @RequestParam(required = false) String logout,
            ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos!");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente!");
        }
        return "login.html";
    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {

        List<Zona> zonas = zonaRepositorio.findAll();
        modelo.put("zonas", zonas);

        return "registro.html";
    }

    // @RequestParam  parametros que viajan en la url y son requeridos
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo,
            MultipartFile archivo,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String mail,
            @RequestParam String clave1,
            @RequestParam String clave2,
            @RequestParam String idZona) {

        // intentamos llamar al metodo registrar de usuario servicio para mandarle la info del formulario registrar
        try {
            usuarioServicio.registrar(archivo, nombre, apellido, mail, clave1, clave2, idZona);
        } catch (ErrorServicio ex) {
            List<Zona> zonas = zonaRepositorio.findAll();
            modelo.put("zonas", zonas);
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "registro.html";
        }
        modelo.put("titulo", "Bienvenido al Tinder de Mascotas");
        modelo.put("descripcion", "Tu usuario fue registrado con exito!");
        return "exito.html";
    }
}
