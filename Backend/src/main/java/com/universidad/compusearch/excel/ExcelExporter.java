package com.universidad.compusearch.excel;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Metrica;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.Usuario;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Component
public class ExcelExporter {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static byte[] exportTiendas(List<Tienda> tiendas, String titulo) throws IOException {
        Workbook workbook = ExcelUtils.createWorkbook();
        Sheet sheet = workbook.createSheet("Tiendas");
        CreationHelper helper = workbook.getCreationHelper();

        ExcelUtils.agregarLogo(sheet, helper, workbook);
        ExcelUtils.createTitle(sheet, titulo, workbook, 6);

        CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
        CellStyle dataStyle = ExcelUtils.createDataStyle(workbook);

        // Cabecera
        Row header = sheet.createRow(8);
        String[] headers = { "ID", "Nombre", "Teléfono", "Descripción", "Fecha Afiliación", "Enlace", "Productos" };

        ExcelUtils.writeRow(header, headerStyle, (Object[]) headers);

        // Datos
        int rowIndex = 9;
        for (Tienda t : tiendas) {
            Row row = sheet.createRow(rowIndex++);
            ExcelUtils.writeRow(row, dataStyle,
                    t.getIdUsuario(),
                    t.getNombre(),
                    t.getTelefono(),
                    t.getDescripcion(),
                    t.getFechaAfiliacion().format(DATE_FORMAT),
                    t.getUrlPagina(),
                    t.getProductos().size());
        }

        ExcelUtils.autoSize(sheet, headers.length);

        return ExcelUtils.writeToBytes(workbook);
    }

    public static byte[] exportResultadosGenericos(List<Object[]> datos, String titulo, String[] headers)
            throws IOException {

        Workbook workbook = ExcelUtils.createWorkbook();
        Sheet sheet = workbook.createSheet("Reporte");
        CreationHelper helper = workbook.getCreationHelper();

        ExcelUtils.agregarLogo(sheet, helper, workbook);
        ExcelUtils.createTitle(sheet, titulo, workbook, headers.length);

        CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
        CellStyle dataStyle = ExcelUtils.createDataStyle(workbook);

        // Cabecera
        Row header = sheet.createRow(8);
        ExcelUtils.writeRow(header, headerStyle, (Object[]) headers);

        // Datos
        int rowIndex = 9;
        for (Object[] fila : datos) {
            Row row = sheet.createRow(rowIndex++);
            ExcelUtils.writeRow(row, dataStyle, fila);
        }

        ExcelUtils.autoSize(sheet, headers.length);

        return ExcelUtils.writeToBytes(workbook);
    }

    public static byte[] exportUsuariosDesdeFecha(List<Usuario> usuarios, String titulo, LocalDateTime fechaInicio)
            throws IOException {

        Workbook workbook = ExcelUtils.createWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios");
        CreationHelper helper = workbook.getCreationHelper();

        ExcelUtils.agregarLogo(sheet, helper, workbook);
        ExcelUtils.createTitle(sheet, titulo, workbook, 5);

        String[] headers = { "ID", "Nombre", "Email", "Fecha Registro", "Estado" };

        CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
        CellStyle dataStyle = ExcelUtils.createDataStyle(workbook);

        Row header = sheet.createRow(8);
        ExcelUtils.writeRow(header, headerStyle, (Object[]) headers);

        int rowIndex = 9;
        for (Usuario u : usuarios) {
            if (!u.getFechaRegistro().isBefore(fechaInicio)) {
                Row row = sheet.createRow(rowIndex++);
                ExcelUtils.writeRow(row, dataStyle,
                        u.getIdUsuario(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getFechaRegistro().format(DATE_FORMAT),
                        u.isActivo() ? "Activo" : "Inactivo");
            }
        }

        ExcelUtils.autoSize(sheet, headers.length);
        return ExcelUtils.writeToBytes(workbook);
    }

    public static byte[] exportUsuariosActivosInactivos(List<Usuario> usuarios, String titulo)
            throws IOException {

        Workbook workbook = ExcelUtils.createWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios Act-Inact");
        CreationHelper helper = workbook.getCreationHelper();

        ExcelUtils.agregarLogo(sheet, helper, workbook);
        ExcelUtils.createTitle(sheet, titulo, workbook, 4);

        String[] headers = { "ID", "Nombre", "Email", "Estado" };
        CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
        CellStyle dataStyle = ExcelUtils.createDataStyle(workbook);

        Row header = sheet.createRow(8);
        ExcelUtils.writeRow(header, headerStyle, (Object[]) headers);

        int rowIndex = 9;
        for (Usuario u : usuarios) {
            Row row = sheet.createRow(rowIndex++);
            ExcelUtils.writeRow(row, dataStyle,
                    u.getIdUsuario(),
                    u.getUsername(),
                    u.getEmail(),
                    u.isActivo() ? "Activo" : "Inactivo");
        }

        ExcelUtils.autoSize(sheet, headers.length);
        return ExcelUtils.writeToBytes(workbook);
    }

    public byte[] exportCatalogoProductos(List<ProductoTienda> productos, String titulo) throws IOException {
        Workbook wb = ExcelUtils.createWorkbook();
        Sheet sheet = wb.createSheet("Catálogo");
        CreationHelper helper = wb.getCreationHelper();

        ExcelUtils.agregarLogo(sheet, helper, wb);
        ExcelUtils.createTitle(sheet, titulo, wb, 8);

        CellStyle headerStyle = ExcelUtils.createHeaderStyle(wb);
        CellStyle dataStyle = ExcelUtils.createDataStyle(wb);
        CellStyle currency = ExcelUtils.createCurrencyStyle(wb);

        Row header = sheet.createRow(8);
        ExcelUtils.writeRow(header, headerStyle,
                "ID", "Nombre", "Marca", "Modelo",
                "Categoría", "Precio", "Stock", "Estado");

        int rowIndex = 9;

        for (ProductoTienda pt : productos) {
            Producto p = pt.getProducto();
            Row row = sheet.createRow(rowIndex++);

            ExcelUtils.writeRow(row, dataStyle,
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getMarca(),
                    p.getModelo(),
                    p.getCategoria().getNombre(),
                    pt.getPrecio(),
                    pt.getStock(),
                    pt.getStock() > 0 ? "Disponible" : "Sin stock");

            row.getCell(5).setCellStyle(currency);
        }

        ExcelUtils.autoSize(sheet, 8);
        return ExcelUtils.writeToBytes(wb);
    }

    public byte[] exportProductosBajoStock(List<ProductoTienda> productos, String titulo, int limiteStock)
            throws IOException {

        Workbook wb = ExcelUtils.createWorkbook();
        Sheet sheet = wb.createSheet("Stock Bajo");
        CreationHelper helper = wb.getCreationHelper();

        ExcelUtils.agregarLogo(sheet, helper, wb);
        ExcelUtils.createTitle(sheet, titulo, wb, 7);

        CellStyle header = ExcelUtils.createHeaderStyle(wb);
        CellStyle data = ExcelUtils.createDataStyle(wb);
        CellStyle currency = ExcelUtils.createCurrencyStyle(wb);

        Row headerRow = sheet.createRow(8);
        ExcelUtils.writeRow(headerRow, header,
                "ID", "Producto", "Marca", "Categoría",
                "Precio", "Stock", "Estado");

        int rowIndex = 9;

        for (ProductoTienda pt : productos) {
            if (pt.getStock() <= limiteStock) {
                Producto p = pt.getProducto();
                Row row = sheet.createRow(rowIndex++);

                ExcelUtils.writeRow(row, data,
                        p.getIdProducto(),
                        p.getNombre(),
                        p.getMarca(),
                        p.getCategoria().getNombre(),
                        pt.getPrecio(),
                        pt.getStock(),
                        pt.getStock() == 0 ? "SIN STOCK" : "BAJO STOCK");

                row.getCell(4).setCellStyle(currency);
            }
        }

        ExcelUtils.autoSize(sheet, 7);
        return ExcelUtils.writeToBytes(wb);
    }

    public byte[] exportMetricasTienda(List<ProductoTienda> productos, String titulo) throws IOException {

        Workbook wb = ExcelUtils.createWorkbook();
        Sheet sheet = wb.createSheet("Métricas");
        CreationHelper helper = wb.getCreationHelper();

        ExcelUtils.agregarLogo(sheet, helper, wb);
        ExcelUtils.createTitle(sheet, titulo, wb, 2);

        int total = productos.size();
        long sinStock = productos.stream().filter(p -> p.getStock() == 0).count();
        long bajoStock = productos.stream().filter(p -> p.getStock() <= 5 && p.getStock() > 0).count();
        double precioProm = productos.stream().mapToDouble(p -> p.getPrecio().doubleValue()).average().orElse(0);

        ProductoTienda max = productos.stream()
                .max(Comparator.comparing(ProductoTienda::getPrecio))
                .orElse(null);

        ProductoTienda min = productos.stream()
                .min(Comparator.comparing(ProductoTienda::getPrecio))
                .orElse(null);

        int totalVisitas = productos.stream()
                .flatMap(p -> p.getMetricas().stream())
                .mapToInt(Metrica::getVisitas)
                .sum();

        int totalClicks = productos.stream()
                .flatMap(p -> p.getMetricas().stream())
                .mapToInt(Metrica::getClicks)
                .sum();

        int totalBuilds = productos.stream()
                .flatMap(p -> p.getMetricas().stream())
                .mapToInt(Metrica::getBuilds)
                .sum();

        double visitasProm = (total == 0) ? 0 : (double) totalVisitas / total;

        String[][] resumen = {
                { "Total productos", String.valueOf(total) },
                { "Sin stock", String.valueOf(sinStock) },
                { "Stock bajo", String.valueOf(bajoStock) },
                { "Precio promedio", "S/. " + precioProm },
                { "Producto más caro", max != null ? max.getProducto().getNombre() : "-" },
                { "Producto más barato", min != null ? min.getProducto().getNombre() : "-" },
                { "Total visitas", String.valueOf(totalVisitas) },
                { "Total clicks", String.valueOf(totalClicks) },
                { "Total builds (armados)", String.valueOf(totalBuilds) },
                { "Promedio visitas por producto", String.format("%.2f", visitasProm) },
        };

        int rowIndex = 9;
        for (String[] fila : resumen) {
            Row row = sheet.createRow(rowIndex++);
            ExcelUtils.writeRow(row, null, fila[0], fila[1]);
        }

        ExcelUtils.autoSize(sheet, 2);
        return ExcelUtils.writeToBytes(wb);
    }

}
