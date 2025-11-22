package com.universidad.compusearch.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Build;
import com.universidad.compusearch.entity.DetalleBuild;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.entity.Usuario;
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

    // Exportar build en excel
    public ByteArrayInputStream exportarBuildAExcel(Long idBuild) throws IOException {
        Build build = buildRepository.findById(idBuild)
                .orElseThrow(() -> {
                    log.error("No se encontró build con ID: {}", idBuild);
                    return BuildException.notFound();
                });

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CreationHelper helper = workbook.getCreationHelper();
            DataFormat format = workbook.createDataFormat();

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(format.getFormat("$#,##0.00"));

            Sheet resumenSheet = workbook.createSheet("Resumen");
            agregarLogo(resumenSheet, helper, workbook);
            escribirResumen(resumenSheet, build, headerStyle, currencyStyle);

            Sheet detalleSheet = workbook.createSheet("Detalle");
            escribirDetalle(detalleSheet, build, headerStyle, currencyStyle);

            workbook.write(out);
            log.info("Exportación de build ID {} a Excel completada.", idBuild);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // Crear estilos del excel de build
    private CellStyle crearEstiloEncabezado(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // Metodo auxiliar para agregar el logo
    private void agregarLogo(Sheet sheet, CreationHelper helper, Workbook workbook) {
        for (int r = 0; r < 6; r++) { 
            Row row = sheet.getRow(r);
            if (row == null)
                row = sheet.createRow(r);
            for (int c = 0; c < 4; c++) {
                Cell cell = row.createCell(c);
                CellStyle bgStyle = workbook.createCellStyle();
                bgStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                bgStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(bgStyle);
            }
        }

        try {
            byte[] imageBytes = CargarImagen.cargarImagen("static/images/util/logo.png");

            if (imageBytes != null) {
                int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
                Drawing<?> drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(0);
                anchor.setRow1(0);
                anchor.setCol2(4);
                anchor.setRow2(6);
                drawing.createPicture(anchor, pictureIdx);
                log.info("Logo agregado correctamente a la hoja de resumen.");
            } else {
                log.warn("No se pudo cargar el logo (bytes nulos).");
            }
        } catch (Exception e) {
            log.warn("Error al agregar el logo: {}", e.getMessage());
        }
    }

    // Metodo auxiliar para agregar un resumen del usuario
    private void escribirResumen(Sheet sheet, Build build, CellStyle headerStyle, CellStyle currencyStyle) {
        int rowIdx = 6;
        Usuario usuario = build.getUsuario();

        CellStyle compatibleStyle = sheet.getWorkbook().createCellStyle();
        Font compFont = sheet.getWorkbook().createFont();
        compFont.setBold(true);
        compatibleStyle.setFont(compFont);
        compatibleStyle.setFillForegroundColor(
                build.isCompatible() ? IndexedColors.LIGHT_GREEN.getIndex() : IndexedColors.ROSE.getIndex());
        compatibleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String compatText = build.isCompatible() ? "Sí" : "No";

        String[][] resumen = {
                { "ID Build", String.valueOf(build.getIdBuild()) },
                { "Nombre Build", build.getNombre() },
                { "Fecha Creación", build.getFechaCreacion() != null ? build.getFechaCreacion().toString() : "N/A" },
                { "Compatible", compatText },
                { "Costo Total", build.getCostoTotal() != null ? build.getCostoTotal().toString() : "$0.00" },
                { "Cantidad de Productos", String.valueOf(build.getDetalles().size()) },
                { "ID Usuario", String.valueOf(usuario.getIdUsuario()) },
                { "Nombre Usuario", usuario.getUsername() },
                { "Correo Usuario", usuario.getEmail() },
                { "Tipo Usuario", usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().name() : "N/A" }
        };

        for (String[] fila : resumen) {
            Row row = sheet.createRow(rowIdx++);
            Cell celdaTitulo = row.createCell(0);
            celdaTitulo.setCellValue(fila[0]);
            celdaTitulo.setCellStyle(headerStyle);
            Cell celdaValor = row.createCell(1);
            celdaValor.setCellValue(fila[1]);
            if ("Compatible".equals(fila[0])) {
                celdaValor.setCellStyle(compatibleStyle);
            }
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    // Metodo auxiliar para escribir el detalle de cada preguntas
    private void escribirDetalle(Sheet sheet, Build build, CellStyle headerStyle, CellStyle currencyStyle) {
        String[] columnasFijas = {
                "Producto", "Marca", "Modelo", "Categoría",
                "Cantidad", "Precio Unitario", "Subtotal"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnasFijas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnasFijas[i]);
            cell.setCellStyle(headerStyle);
        }

        Cell attrNameHeader = headerRow.createCell(columnasFijas.length);
        attrNameHeader.setCellValue("Atributo");
        attrNameHeader.setCellStyle(headerStyle);

        Cell attrValueHeader = headerRow.createCell(columnasFijas.length + 1);
        attrValueHeader.setCellValue("Valor");
        attrValueHeader.setCellStyle(headerStyle);

        List<DetalleBuild> detalles = build.getDetalles();
        int rowIdx = 1;
        BigDecimal totalBuild = BigDecimal.ZERO;

        for (DetalleBuild detalle : detalles) {
            Producto producto = detalle.getProductoTienda().getProducto();
            List<ProductoAtributo> atributos = producto.getAtributos();
            int startRow = rowIdx;

            for (ProductoAtributo atributo : atributos) {
                Row row = sheet.createRow(rowIdx++);
                if (row.getRowNum() == startRow) {
                    row.createCell(0).setCellValue(producto.getNombre());
                    row.createCell(1).setCellValue(producto.getMarca());
                    row.createCell(2).setCellValue(producto.getModelo());
                    row.createCell(3).setCellValue(producto.getCategoria().getNombre());
                    row.createCell(4).setCellValue(detalle.getCantidad());

                    BigDecimal precioUnitario = detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario() : BigDecimal.ZERO;
                    Cell precioCell = row.createCell(5);
                    precioCell.setCellValue(precioUnitario.doubleValue());
                    precioCell.setCellStyle(currencyStyle);

                    BigDecimal subtotal = detalle.getSubTotal() != null ? detalle.getSubTotal() : BigDecimal.ZERO;
                    Cell subtotalCell = row.createCell(6);
                    subtotalCell.setCellValue(subtotal.doubleValue());
                    subtotalCell.setCellStyle(currencyStyle);

                    totalBuild = totalBuild.add(subtotal);
                }

                row.createCell(7).setCellValue(atributo.getAtributo().getNombre());
                row.createCell(8).setCellValue(atributo.getValor());
            }

            if (atributos.isEmpty()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(producto.getNombre());
                row.createCell(1).setCellValue(producto.getMarca());
                row.createCell(2).setCellValue(producto.getModelo());
                row.createCell(3).setCellValue(producto.getCategoria().getNombre());
                row.createCell(4).setCellValue(detalle.getCantidad());

                BigDecimal precioUnitario = detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario() : BigDecimal.ZERO;
                Cell precioCell = row.createCell(5);
                precioCell.setCellValue(precioUnitario.doubleValue());
                precioCell.setCellStyle(currencyStyle);

                BigDecimal subtotal = detalle.getSubTotal() != null ? detalle.getSubTotal() : BigDecimal.ZERO;
                Cell subtotalCell = row.createCell(6);
                subtotalCell.setCellValue(subtotal.doubleValue());
                subtotalCell.setCellStyle(currencyStyle);

                totalBuild = totalBuild.add(subtotal);
            }

            rowIdx++;
        }

        Row totalRow = sheet.createRow(rowIdx++);
        Cell totalLabel = totalRow.createCell(5);
        totalLabel.setCellValue("TOTAL:");
        totalLabel.setCellStyle(headerStyle);

        Cell totalCell = totalRow.createCell(6);
        totalCell.setCellValue(totalBuild.doubleValue());
        totalCell.setCellStyle(currencyStyle);

        for (int i = 0; i <= 8; i++) {
            sheet.autoSizeColumn(i);
        }
    }

}
