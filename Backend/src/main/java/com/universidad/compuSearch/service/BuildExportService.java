package com.universidad.compusearch.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Build;
import com.universidad.compusearch.entity.DetalleBuild;
import com.universidad.compusearch.exception.BuildException;
import com.universidad.compusearch.repository.BuildRepository;
import com.universidad.compusearch.util.CargarImagen;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuildExportService {

    private final BuildRepository buildRepository;

    public ByteArrayInputStream exportarBuildAExcel(Long idBuild) throws IOException {
        String[] columnas = {
                "ID Build", "Nombre Build", "Compatible", "Costo Total", "ID Usuario",
                "Producto", "Cantidad", "Precio Unitario", "Subtotal"
        };

        Build build = buildRepository.findById(idBuild)
                .orElseThrow(() -> {
                    log.error("No se encontró build con ID: {}", idBuild);
                    return BuildException.notFound();
                });

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Build " + idBuild);

            // Estilo encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Estilo moneda
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("$#,##0.00"));

            // Crear encabezado
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Escribir datos de la build
            List<DetalleBuild> detalles = build.getDetalles();
            int rowIdx = 1;
            BigDecimal totalBuild = BigDecimal.ZERO;

            Drawing<?> drawing = sheet.createDrawingPatriarch(); // para imágenes
            CreationHelper helper = workbook.getCreationHelper();

            if (detalles.isEmpty()) {
                Row row = sheet.createRow(rowIdx++);
                escribirFila(row, build, null, currencyStyle, drawing, helper);
            } else {
                for (DetalleBuild detalle : detalles) {
                    Row row = sheet.createRow(rowIdx++);
                    escribirFila(row, build, detalle, currencyStyle, drawing, helper);

                    BigDecimal subtotal = detalle.getSubTotal() != null ? detalle.getSubTotal() : BigDecimal.ZERO;
                    totalBuild = totalBuild.add(subtotal);
                }
            }

            // Fila de total general
            Row totalRow = sheet.createRow(rowIdx++);
            Cell totalLabel = totalRow.createCell(7);
            totalLabel.setCellValue("TOTAL BUILD:");
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            CellStyle boldStyle = workbook.createCellStyle();
            boldStyle.setFont(boldFont);
            totalLabel.setCellStyle(boldStyle);

            Cell totalCell = totalRow.createCell(8);
            totalCell.setCellValue(totalBuild.doubleValue());
            CellStyle totalStyle = workbook.createCellStyle();
            totalStyle.setFont(boldFont);
            totalStyle.setDataFormat(format.getFormat("$#,##0.00"));
            totalCell.setCellStyle(totalStyle);

            // Autoajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            log.info("Exportación de build ID {} a Excel completada.", idBuild);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void escribirFila(Row row, Build build, DetalleBuild detalle, CellStyle currencyStyle, Drawing<?> drawing,
            CreationHelper helper) {
        row.createCell(0).setCellValue(build.getIdBuild());
        row.createCell(1).setCellValue(build.getNombre());
        row.createCell(2).setCellValue(build.isCompatible() ? "Sí" : "No");

        BigDecimal costo = build.getCostoTotal() != null ? build.getCostoTotal() : BigDecimal.ZERO;
        Cell costoCell = row.createCell(3);
        costoCell.setCellValue(costo.doubleValue());
        costoCell.setCellStyle(currencyStyle);

        row.createCell(4).setCellValue(build.getUsuario().getIdUsuario());

        if (detalle != null && detalle.getProductoTienda() != null) {
            String nombreProducto = detalle.getProductoTienda().getProducto() != null
                    ? detalle.getProductoTienda().getProducto().getNombre()
                    : "N/A";
            row.createCell(5).setCellValue(nombreProducto);

            // Agregar imagen en la columna del producto (columna 5)
            try {
                byte[] imageBytes = CargarImagen.cargarImagen("static/images/util/logo.webp");
                if (imageBytes != null) {
                    int pictureIdx = row.getSheet().getWorkbook().addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setCol1(5);
                    anchor.setRow1(row.getRowNum());
                    Picture pict = drawing.createPicture(anchor, pictureIdx);
                    pict.resize(1.0, 1.0); // ajusta imagen al tamaño de la celda
                }
            } catch (Exception e) {
                log.warn("No se pudo cargar la imagen para el producto '{}': {}", nombreProducto, e.getMessage());
            }

            row.createCell(6).setCellValue(detalle.getCantidad());

            BigDecimal precioUnitario = detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario()
                    : BigDecimal.ZERO;
            Cell precioCell = row.createCell(7);
            precioCell.setCellValue(precioUnitario.doubleValue());
            precioCell.setCellStyle(currencyStyle);

            BigDecimal subtotal = detalle.getSubTotal() != null ? detalle.getSubTotal() : BigDecimal.ZERO;
            Cell subtotalCell = row.createCell(8);
            subtotalCell.setCellValue(subtotal.doubleValue());
            subtotalCell.setCellStyle(currencyStyle);
        }
    }
}
