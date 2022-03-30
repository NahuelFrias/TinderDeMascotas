package com.nahuel.Tinder.configuraciones;

import com.nahuel.Tinder.servicios.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class ConfiguracionesSeguridad extends WebSecurityConfigurerAdapter{
    
    //busca un usuario por nombre de usuario en la base de datos
    @Autowired
    public UsuarioServices usuarioServicio;
    
    // configuracion del manejador de seguridad de Spring security
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioServicio) //le decimos cual es el servicio para auntenticar un cliente,
                .passwordEncoder(new BCryptPasswordEncoder()); //luego de que encuentra el usuario le decimos cual es el encoder para comparar contraseñas
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                            .antMatchers("/css/*", "/js/*", "/img/*", "/**")//cualquier usuario sin estar logueado
                            .permitAll()                                                        //puede acceder a estos archivos
                .and().formLogin()//configuramos el login                                                             
                            .loginPage("/login") // Donde esta mi login
                            .loginProcessingUrl("/logincheck")//url que autentica un cliente
                            .usernameParameter("username") // Con que nombre viajan los datos del logueo
                            .passwordParameter("password")
                            .defaultSuccessUrl("/inicio") // A que URL ingresa si el usuario se autentica con exito
                            .permitAll()
                .and().logout() // Aca configuro la salida
                            .logoutUrl("/logout")//sprin security desloguea desde esta url
                            .logoutSuccessUrl("/login?logout")//y nos redirige aca
                            .permitAll()
                .and().csrf().disable();
                //con estas lineas no me manda el parametro de logout y no llega el msj de deslogueado
    }
}
