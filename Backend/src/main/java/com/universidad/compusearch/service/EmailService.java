package com.universidad.compusearch.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Pago;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        // Asunto del correo
        String subject = "Recuperación de contraseña - CompuSearch";

        // URL de la página con el token de recuperación
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;

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

    public void sendInvoiceEmail(String to, Pago pago, byte[] pdfBytes) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Factura de tu Suscripción - CompuSearch");

            String body = """
                    Hola,

                    Gracias por tu pago. Adjuntamos la factura correspondiente.

                    Detalles del pago:
                    - Monto: %s
                    - Fecha: %s
                    - ID de Operación: %s
                    - Plan: %s

                    Si tienes alguna consulta, no dudes en responder este correo.

                    Atentamente,
                    CompuSearch
                    """.formatted(
                    pago.getMonto(),
                    pago.getFechaPago(),
                    pago.getIdOperacion(),
                    pago.getSuscripcion().getPlan().getNombre());

            helper.setText(body);

            helper.addAttachment("factura-" + pago.getIdOperacion() + ".pdf",
                    new ByteArrayResource(pdfBytes));

            mailSender.send(message);

            log.info("Factura enviada correctamente a {}", to);

        } catch (Exception e) {
            log.error("Error al enviar factura al correo {}", to, e);
            throw new RuntimeException("Error al enviar factura", e);
        }
    }
}
