package com.nahuel.Tinder.controladores;

import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.servicios.UsuarioServices;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("")
@RequestMapping("/") // url que escucha este controlador
public class PortalControlador {
    
    @Autowired
    UsuarioServices usuarioServicio;
    
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    
    @GetMapping("/login")
    public String login(){
        return "login.html";
    }
    
    @GetMapping("/registro")
    public String registro(){
        return "registro.html";
    }
    // @RequestParam  parametros que viajan en la url y son requeridos
    @PostMapping("/registrar")
    public String registrar(@RequestParam String nombre,
            @RequestParam  String apellido,
            @RequestParam  String mail,
            @RequestParam  String clave1,
            @RequestParam  String clave2){
        
        // intentamos llamar al metodo registrar de usuario servicio para mandarle la info del formulario registrar
        try {
            usuarioServicio.registrar(null, nombre, apellido, mail, clave1);
        } catch (ErrorServicio ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "registro.html";
        }
        return "index.html";
    }
}
