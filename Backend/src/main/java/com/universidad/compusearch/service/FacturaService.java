package com.universidad.compusearch.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.universidad.compusearch.entity.Pago;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@Slf4j
public class FacturaService {

    public byte[] generarFacturaPdf(Pago pago) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, baos);
            document.open();

            try {
                Image logo = Image.getInstance(getClass().getResource("/static/logo.png"));

                logo.scaleToFit(120, 120);
                logo.setAlignment(Image.ALIGN_CENTER);

                document.add(logo);
                document.add(new Paragraph("\n"));

            } catch (Exception ex) {
                log.warn("No se pudo cargar el logo: {}", ex.getMessage());
            }

            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("FACTURA DE SUSCRIPCIÓN\n\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font textFont = new Font(Font.HELVETICA, 12);

            document.add(new Paragraph("CompuSearch S.A.C", labelFont));
            document.add(new Paragraph("RUC: 12345678910", textFont));
            document.add(new Paragraph("Av. Lima 123, Perú\n\n", textFont));

            document.add(new LineSeparator());

            document.add(new Paragraph("\nDetalles del Cliente", labelFont));
            document.add(new Paragraph("Tienda: " + pago.getSuscripcion().getTienda().getNombre(), textFont));
            document.add(new Paragraph("Correo: " + pago.getSuscripcion().getTienda().getEmail(), textFont));
            document.add(new Paragraph("\n"));

            document.add(new LineSeparator());
            document.add(new Paragraph("\nDatos del Pago", labelFont));

            document.add(new Paragraph("ID Operación: " + pago.getIdOperacion(), textFont));
            document.add(new Paragraph("Fecha: " + pago.getFechaPago(), textFont));
            document.add(new Paragraph("Plan: " + pago.getSuscripcion().getPlan().getNombre(), textFont));
            document.add(new Paragraph("Monto: S/ " + pago.getMonto(), textFont));
            document.add(new Paragraph("\n"));

            document.add(new LineSeparator());
            document.add(new Paragraph("\nGracias por su compra.\n\n", textFont));

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error al generar PDF de factura", e);
            throw new RuntimeException("Error generando factura PDF", e);
        }
    }
}