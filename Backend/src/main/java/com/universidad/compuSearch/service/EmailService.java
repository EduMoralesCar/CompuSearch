package com.universidad.compusearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendPasswordResetEmail(String to, String token) {

        // Asunto del correo
        String subject = "Recuperación de contraseña - CompuSearch";

        // URL de la página con el token de recuperación
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;

        // Cuerpo del mensaje en texto plano
        String text = "Hola,\n\n" +
                "Has solicitado restablecer tu contraseña. Usa el siguiente enlace para hacerlo:\n" +
                resetUrl + "\n\n" +
                "Este enlace expirará en 1 hora.\n\n" +
                "Si no solicitaste este cambio, ignora este mensaje.";

        try {
            // Crear mensaje de correo
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            // Enviar correo
            mailSender.send(message);

            logger.info("Correo de restablecimiento enviado a: {}", to);
        } catch (Exception e) {
            logger.error("Error al enviar correo de restablecimiento a {}", to, e);
            throw e;
        }
    }
}