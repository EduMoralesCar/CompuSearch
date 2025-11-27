package com.universidad.compusearch.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.universidad.compusearch.util.CargarImagen;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class ExcelUtils {
    public static Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    public static CellStyle createHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        addBorders(style);
        return style;
    }

    public static CellStyle createDataStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        addBorders(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    public static CellStyle createTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font titleFont = wb.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);

        style.setFont(titleFont);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    public static CellStyle createCurrencyStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("[$S/. ]#,##0.00"));
        return style;
    }

    private static void addBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    public static void autoSize(Sheet sheet, int cols) {
        for (int i = 0; i < cols; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public static void writeRow(Row row, CellStyle style, Object... values) {
        int col = 0;
        for (Object v : values) {
            Cell cell = row.createCell(col++);
            if (v instanceof Number) {
                cell.setCellValue(((Number) v).doubleValue());
            } else {
                cell.setCellValue(v != null ? v.toString() : "");
            }
            if (style != null)
                cell.setCellStyle(style);
        }
    }

    public static void createTitle(Sheet sheet, String title, Workbook wb, int colSpan) {
        Row titleRow = sheet.createRow(7);

        CellStyle style = createTitleStyle(wb);
        Cell cell = titleRow.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, colSpan - 1));
    }

    public static byte[] writeToBytes(Workbook wb) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        return out.toByteArray();
    }

    public static void agregarLogo(Sheet sheet, CreationHelper helper, Workbook workbook) {

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
            byte[] imageBytes = CargarImagen.cargarImagen("static/logo.png");

            if (imageBytes != null) {
                int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
                Drawing<?> drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();

                anchor.setCol1(0);
                anchor.setRow1(0);
                anchor.setCol2(4);
                anchor.setRow2(6);

                drawing.createPicture(anchor, pictureIdx);

                log.info("Logo agregado correctamente a la hoja.");
            } else {
                log.warn("No se pudo cargar el logo (bytes nulos).");
            }

        } catch (Exception e) {
            log.warn("Error al agregar el logo: {}", e.getMessage());
        }
    }
}
