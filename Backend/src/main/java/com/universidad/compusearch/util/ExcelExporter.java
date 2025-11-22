package com.universidad.compusearch.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.universidad.compusearch.entity.Tienda;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExporter {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static byte[] exportTiendas(List<Tienda> tiendas, String titulo) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte");

        crearTitulo(workbook, sheet, titulo);
        crearCabeceraTiendas(workbook, sheet);

        int rowIndex = 2;
        CellStyle dataStyle = crearDataStyle(workbook);

        for (Tienda tienda : tiendas) {
            Row row = sheet.createRow(rowIndex++);

            int col = 0;

            Cell cellId = row.createCell(col++);
            cellId.setCellValue(tienda.getIdUsuario());
            cellId.setCellStyle(dataStyle);

            Cell cellNombre = row.createCell(col++);
            cellNombre.setCellValue(tienda.getNombre());
            cellNombre.setCellStyle(dataStyle);

            Cell cellTelefono = row.createCell(col++);
            cellTelefono.setCellValue(tienda.getTelefono());
            cellTelefono.setCellStyle(dataStyle);

            Cell cellDescripcion = row.createCell(col++);
            cellDescripcion.setCellValue(tienda.getDescripcion());
            cellDescripcion.setCellStyle(dataStyle);

            Cell cellFechaAfiliacion = row.createCell(col++);
            cellFechaAfiliacion.setCellValue(tienda.getFechaAfiliacion().format(DATE_FORMAT));
            cellFechaAfiliacion.setCellStyle(dataStyle);

            Cell cellUrlPagina = row.createCell(col++);
            cellUrlPagina.setCellValue(tienda.getUrlPagina());
            cellUrlPagina.setCellStyle(dataStyle);
        }

        autoSize(sheet, 4);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        workbook.write(output);
        workbook.close();
        return output.toByteArray();
    }

    public static byte[] exportResultadosGenericos(List<Object[]> datos, String titulo, String[] headers)
            throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte");

        crearTitulo(workbook, sheet, titulo);

        Row headerRow = sheet.createRow(1);
        CellStyle headerStyle = crearHeaderStyle(workbook);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 2;
        CellStyle dataStyle = crearDataStyle(workbook);

        for (Object[] fila : datos) {
            Row row = sheet.createRow(rowIndex++);

            for (int i = 0; i < fila.length; i++) {
                Cell cell = row.createCell(i);
                if (fila[i] instanceof Number) {
                    cell.setCellValue(((Number) fila[i]).doubleValue());
                } else {
                    cell.setCellValue(String.valueOf(fila[i]));
                }
                cell.setCellStyle(dataStyle);
            }
        }

        autoSize(sheet, headers.length);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        workbook.write(output);
        workbook.close();
        return output.toByteArray();
    }

    private static void crearTitulo(Workbook workbook, Sheet sheet, String titulo) {
        Row titleRow = sheet.createRow(0);

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);

        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(titulo);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                0, 0, 0, 5));
    }

    private static void crearCabeceraTiendas(Workbook workbook, Sheet sheet) {
        Row headerRow = sheet.createRow(1);
        CellStyle style = crearHeaderStyle(workbook);

        String[] headers = { "ID", "Nombre", "Teléfono", "Descripción", "Fecha de afiliacion", "Enlace de la Pagina" };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private static CellStyle crearHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        agregarBordes(style);

        return style;
    }

    private static CellStyle crearDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        agregarBordes(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private static void agregarBordes(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    private static void autoSize(Sheet sheet, int columnas) {
        for (int i = 0; i < columnas; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
