package com.universidad.compusearch.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado del envío de correos electrónicos.
 * 
 * Actualmente soporta el envío de correos para recuperación de contraseña,
 * pero puede extenderse para otros tipos de notificaciones.
 * 
 * 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Envía un correo electrónico de restablecimiento de contraseña a un usuario.
     *
     * @param to    la dirección de correo del destinatario.
     * @param token el token único de recuperación de contraseña que se incluirá en el enlace.
     * @throws RuntimeException si ocurre un error durante el envío del correo.
     */
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

            log.info("Correo de restablecimiento enviado a: {}", to);
        } catch (Exception e) {
            log.error("Error al enviar correo de restablecimiento a {}", to, e);
            throw new RuntimeException("Error al enviar correo de recuperación de contraseña", e);
        }
    }
}
