package com.nahuel.Tinder;

import com.nahuel.Tinder.servicios.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TinderApplication extends WebSecurityConfigurerAdapter {
    
    // defino cual es el servicio que hace la validacion del servicio
    @Autowired
    private UsuarioServices usuarioServicio;

    public static void main(String[] args) {
        SpringApplication.run(TinderApplication.class, args);
    }
    
    //este metodo le dice a SpringSecurity cual es el servicio
    //que va a utilizar para autentificar al usuario
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
    }//y setea un encriptador de claves
}
