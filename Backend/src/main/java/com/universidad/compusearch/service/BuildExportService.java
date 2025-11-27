package com.universidad.compusearch.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Build;
import com.universidad.compusearch.entity.DetalleBuild;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.excel.ExcelUtils;
import com.universidad.compusearch.exception.BuildException;
import com.universidad.compusearch.repository.BuildRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuildExportService {

    private final BuildRepository buildRepository;

    public ByteArrayInputStream exportarBuildAExcel(Long idBuild) throws IOException {
        Build build = buildRepository.findById(idBuild)
                .orElseThrow(() -> BuildException.notFound());

        Workbook workbook = ExcelUtils.createWorkbook();
        CreationHelper helper = workbook.getCreationHelper();

        CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
        CellStyle currencyStyle = ExcelUtils.createCurrencyStyle(workbook);

        Sheet resumenSheet = workbook.createSheet("Resumen");
        ExcelUtils.agregarLogo(resumenSheet, helper, workbook);
        escribirResumen(resumenSheet, build, headerStyle, currencyStyle);

        Sheet detalleSheet = workbook.createSheet("Detalle");
        escribirDetalle(detalleSheet, build, headerStyle, currencyStyle);

        byte[] bytes = ExcelUtils.writeToBytes(workbook);
        return new ByteArrayInputStream(bytes);
    }

    /** ------------------ RESUMEN ------------------ **/
    private void escribirResumen(Sheet sheet, Build build,
            CellStyle headerStyle, CellStyle currencyStyle) {

        int rowIdx = 7;

        String[][] resumen = {
                { "ID Build", String.valueOf(build.getIdBuild()) },
                { "Nombre", build.getNombre() },
                { "Fecha Creación", build.getFechaCreacion() != null ? build.getFechaCreacion().toString() : "N/A" },
                { "Compatible", build.isCompatible() ? "Sí" : "No" },
                { "Costo Total (S/.)", build.getCostoTotal() != null ? build.getCostoTotal().toString() : "S/. 0.00" },
                { "# Productos", String.valueOf(build.getDetalles().size()) },
                { "ID Usuario", Long.toString(build.getUsuario().getIdUsuario()) },
                { "Usuario", build.getUsuario().getUsername() },
                { "Correo", build.getUsuario().getEmail() },
                { "Tipo Usuario",
                        build.getUsuario().getTipoUsuario() != null ? build.getUsuario().getTipoUsuario().name()
                                : "N/A" }
        };

        for (String[] item : resumen) {
            Row row = sheet.createRow(rowIdx++);
            ExcelUtils.writeRow(row, null, item[0], item[1]);

            row.getCell(0).setCellStyle(headerStyle);

            if (item[0].equals("Compatible")) {
                CellStyle comp = sheet.getWorkbook().createCellStyle();
                comp.cloneStyleFrom(headerStyle);
                comp.setFillForegroundColor(build.isCompatible()
                        ? IndexedColors.LIGHT_GREEN.getIndex()
                        : IndexedColors.ROSE.getIndex());
                comp.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                row.getCell(1).setCellStyle(comp);
            }
        }

        ExcelUtils.autoSize(sheet, 2);
    }

    /** ------------------ DETALLE ------------------ **/
    private void escribirDetalle(Sheet sheet, Build build,
            CellStyle headerStyle, CellStyle currencyStyle) {

        String[] headers = {
                "Producto", "Marca", "Modelo", "Categoría",
                "Cantidad", "Precio Unitario", "Subtotal",
                "Atributo", "Valor"
        };

        // Header
        Row headerRow = sheet.createRow(0);
        ExcelUtils.writeRow(headerRow, headerStyle, (Object[]) headers);

        int rowIdx = 1;

        for (DetalleBuild detalle : build.getDetalles()) {
            Producto producto = detalle.getProductoTienda().getProducto();

            List<ProductoAtributo> atributos = producto.getAtributos();
            int startRow = rowIdx;

            for (ProductoAtributo atributo : atributos) {
                Row row = sheet.createRow(rowIdx++);

                if (row.getRowNum() == startRow) {
                    ExcelUtils.writeRow(row, null,
                            producto.getNombre(),
                            producto.getMarca(),
                            producto.getModelo(),
                            producto.getCategoria().getNombre(),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario(),
                            detalle.getSubTotal());

                    row.getCell(5).setCellStyle(currencyStyle);
                    row.getCell(6).setCellStyle(currencyStyle);
                }

                row.createCell(7).setCellValue(atributo.getAtributo().getNombre());
                row.createCell(8).setCellValue(atributo.getValor());
            }

            if (atributos.isEmpty()) {
                Row row = sheet.createRow(rowIdx++);

                ExcelUtils.writeRow(row, null,
                        producto.getNombre(),
                        producto.getMarca(),
                        producto.getModelo(),
                        producto.getCategoria().getNombre(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario(),
                        detalle.getSubTotal(),
                        "",
                        "");

                row.getCell(5).setCellStyle(currencyStyle);
                row.getCell(6).setCellStyle(currencyStyle);
            }
        }

        ExcelUtils.autoSize(sheet, 9);

        int extra = 1500;
        sheet.setColumnWidth(5, sheet.getColumnWidth(5) + extra); // Precio Unitario
        sheet.setColumnWidth(6, sheet.getColumnWidth(6) + extra); // Subtotal
    }
}
