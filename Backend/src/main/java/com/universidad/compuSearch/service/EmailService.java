package com.universidad.compuSearch.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // Metodo para enviar el reset token
    public void sendPasswordResetEmail(String to, String token) {

        // Asunto del correo
        String subject = "Recuperación de contraseña - CompuSearch";

        // URL de la pagina con el refresh token
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;

        // Cuerpo del mensaje
        String text = "Hola,\n\n" +
                "Has solicitado restablecer tu contraseña. Usa el siguiente enlace para hacerlo:\n" +
                resetUrl + "\n\n" +
                "Este enlace expirará en 1 hora.\n\n" +
                "Si no solicitaste este cambio, ignora este mensaje.";

        // Se crea un mensaje y se le asignan los datos
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        // Se envia el correo
        mailSender.send(message);
    }
}
