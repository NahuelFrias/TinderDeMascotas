package com.nahuel.Tinder.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("")
@RequestMapping("/") // url que escucha este controlador
public class PortalControlador {
    
    @GetMapping
    public String index(){
        return "index.html";
    }
    
}
