package com.nahuel.Tinder.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificacionServices {

    @Autowired
    private JavaMailSender mailSender; // librería que configuramos cuando creamos el proyecto

    @Async // el hilo de ejecución se ejecuta en un hilo paralelo, así el usuario no tiene que esperar que el mail se envie
    public void enviar(String cuerpo, String titulo, String mail) {
        SimpleMailMessage mensaje = new SimpleMailMessage(); // clase de la lbreria JavaMailSender para enviar mails
        mensaje.setTo(mail); // hacia quien esta dirigido el mail
        mensaje.setFrom("noreply@tinder-mascotas.com"); // quien envía el mail
        mensaje.setSubject(titulo); // titulo
        mensaje.setText(cuerpo); // cuerpo del mail
        
        mailSender.send(mensaje);
    }
}
