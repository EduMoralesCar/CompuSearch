package com.universidad.compusearch.util;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.universidad.compusearch.dto.BuildsInfoResponse;
import com.universidad.compusearch.dto.DetalleAtributoResponse;
import com.universidad.compusearch.dto.DetalleBuildResponse;
import com.universidad.compusearch.dto.EmpleadoResponse;
import com.universidad.compusearch.dto.PlanResponse;
import com.universidad.compusearch.dto.ProductoBuildResponse;
import com.universidad.compusearch.dto.ProductoInfoResponse;
import com.universidad.compusearch.dto.ProductoTiendaInfoResponse;
import com.universidad.compusearch.dto.ProductoTiendaResponse;
import com.universidad.compusearch.dto.SolicitudTiendaResponse;
import com.universidad.compusearch.dto.TiendaDetallesResponse;
import com.universidad.compusearch.dto.TiendaInfoDetalleResponse;
import com.universidad.compusearch.dto.TiendaInfoResponse;
import com.universidad.compusearch.dto.TiendaResponse;
import com.universidad.compusearch.dto.TiendaSuscripcionActualInfoResponse;
import com.universidad.compusearch.dto.UsuarioInfoResponse;
import com.universidad.compusearch.dto.UsuarioResponse;
import com.universidad.compusearch.entity.Build;
import com.universidad.compusearch.entity.DetalleBuild;
import com.universidad.compusearch.entity.Empleado;
import com.universidad.compusearch.entity.Plan;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.SolicitudTienda;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.Usuario;

public class Mapper {

    public static ProductoTiendaResponse mapToProductoTienda(ProductoTienda productoTienda) {
        return new ProductoTiendaResponse(
                productoTienda.getIdProductoTienda(),
                productoTienda.getProducto().getNombre(),
                productoTienda.getPrecio(),
                productoTienda.getStock(),
                productoTienda.getUrlImagen(),
                productoTienda.getTienda().getNombre());
    }

    public static ProductoInfoResponse mapToInfoProducto(ProductoTienda productoTienda) {
        ProductoInfoResponse dto = new ProductoInfoResponse();
        dto.setIdProductoTienda(productoTienda.getIdProductoTienda());
        dto.setNombreProducto(productoTienda.getProducto().getNombre());
        dto.setMarca(productoTienda.getProducto().getMarca());
        dto.setModelo(productoTienda.getProducto().getModelo());
        dto.setDescripcion(productoTienda.getProducto().getDescripcion());
        dto.setUrlImagen(productoTienda.getUrlImagen());
        dto.setNombreTienda(productoTienda.getTienda().getNombre());
        dto.setStock(productoTienda.getStock());
        dto.setPrecio(productoTienda.getPrecio());
        dto.setUrlProducto(productoTienda.getUrlProducto());

        if (productoTienda.getProducto().getAtributos() != null) {
            dto.setAtributos(mapToDetalleAtributo(productoTienda.getProducto().getAtributos()));
        } else {
            dto.setAtributos(Collections.emptyList());
        }

        return dto;
    }

    public static ProductoBuildResponse mapToProductoBuildResponse(ProductoTienda productoTienda) {
        ProductoBuildResponse dto = new ProductoBuildResponse();
        dto.setIdProductoTienda(productoTienda.getIdProductoTienda());
        dto.setNombreProducto(productoTienda.getProducto().getNombre());
        dto.setPrecio(productoTienda.getPrecio());
        dto.setStock(productoTienda.getStock());
        dto.setNombreTienda(productoTienda.getTienda().getNombre());
        dto.setUrlProducto(productoTienda.getUrlProducto());

        if (productoTienda.getProducto().getAtributos() != null) {
            List<DetalleAtributoResponse> atributos = productoTienda.getProducto()
                    .getAtributos()
                    .stream()
                    .map(attr -> new DetalleAtributoResponse(attr.getAtributo().getNombre(), attr.getValor()))
                    .collect(Collectors.toList());
            dto.setDetalles(atributos);
        } else {
            dto.setDetalles(Collections.emptyList());
        }

        return dto;
    }

    public static List<DetalleAtributoResponse> mapToDetalleAtributo(List<ProductoAtributo> atributos) {
        return atributos.stream()
                .map(atributo -> {
                    DetalleAtributoResponse dto = new DetalleAtributoResponse();
                    dto.setNombreAtributo(atributo.getAtributo().getNombre());
                    dto.setValor(atributo.getValor());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public static UsuarioResponse mapToUsuario(Usuario usuario) {
        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .activo(usuario.isActivo())
                .fechaRegistro(usuario.getFechaRegistro())
                .cantidadBuilds(usuario.getBuilds().size())
                .cantidadIncidentes(usuario.getIncidentes().size())
                .cantidadSolicitudes(usuario.getSolicitudes().size())
                .build();
    }

    public static UsuarioInfoResponse mapToUsuarioInfo(Usuario usuario) {
        return new UsuarioInfoResponse(
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getFechaRegistro(),
                usuario.getBuilds().size(),
                usuario.getIncidentes().size(),
                usuario.getSolicitudes().size());
    }

    public static TiendaResponse mapToTienda(Tienda tienda) {
        TiendaResponse dto = new TiendaResponse(
                tienda.getNombre(),
                tienda.getDescripcion(),
                tienda.getTelefono(),
                tienda.getDireccion(),
                Base64.getEncoder().encodeToString(tienda.getLogo()),
                tienda.getUrlPagina(),
                tienda.getEtiquetas());

        return dto;
    }

    public static SolicitudTiendaResponse mapToSolicitudTienda(SolicitudTienda solicitudTienda) {
        SolicitudTiendaResponse dto = new SolicitudTiendaResponse(
                solicitudTienda.getIdSolicitud(),
                solicitudTienda.getUsuario().getIdUsuario(),
                solicitudTienda.getUsuario().getUsername(),
                solicitudTienda.getDatosFormulario(),
                solicitudTienda.getFechaSolicitud(),
                solicitudTienda.getEstado().name(),
                solicitudTienda.getEmpleado() != null ? solicitudTienda.getEmpleado().getIdUsuario() : null,
                solicitudTienda.getEmpleado() != null ? solicitudTienda.getEmpleado().getNombre() : null);

        return dto;
    }

    public static EmpleadoResponse mapToEmpleado(Empleado empleado) {
        EmpleadoResponse dto = new EmpleadoResponse();
        dto.setIdUsuario(empleado.getIdUsuario());
        dto.setUsername(empleado.getUsername());
        dto.setEmail(empleado.getEmail());
        dto.setActivo(empleado.isActivo());
        dto.setFechaRegistro(empleado.getFechaRegistro());
        dto.setPassword(empleado.getPassword());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setRol(empleado.getRol());
        dto.setFechaAsignacion(empleado.getFechaAsignacion());
        return dto;
    }

    public static PlanResponse mapToPlan(Plan plan) {
        PlanResponse dto = new PlanResponse();
        dto.setIdPlan(plan.getIdPlan());
        dto.setNombre(plan.getNombre());
        dto.setDuracionMeses(plan.getDuracionMeses());
        dto.setPrecioMensual(plan.getPrecioMensual());
        dto.setDescripcion(plan.getDescripcion());
        dto.setBeneficios(plan.getBeneficios());
        dto.setSuscripciones(plan.getSuscripciones().size());
        dto.setActivo(plan.isActivo());
        return dto;
    }

    public static BuildsInfoResponse mapToBuildsInfo(Build build) {
        BuildsInfoResponse dto = new BuildsInfoResponse();
        dto.setIdBuild(build.getIdBuild());
        dto.setNombre(build.getNombre());
        dto.setIdUsuario(build.getUsuario().getIdUsuario());
        dto.setCompatible(build.isCompatible());
        dto.setCostoTotal(build.getCostoTotal());
        dto.setDetalles(mapToDetalleBuild(build.getDetalles()));
        return dto;
    }

    public static List<DetalleBuildResponse> mapToDetalleBuild(List<DetalleBuild> detalles) {
        return detalles.stream()
                .map(detalle -> {
                    DetalleBuildResponse dto = new DetalleBuildResponse();

                    dto.setIdProductoTienda(detalle.getProductoTienda().getIdProductoTienda());
                    dto.setNombreProducto(detalle.getProductoTienda().getProducto().getNombre());
                    dto.setNombreTienda(detalle.getProductoTienda().getTienda().getNombre());
                    dto.setStock(detalle.getProductoTienda().getStock());
                    dto.setPrecio(detalle.getProductoTienda().getPrecio());
                    dto.setSubTotal(detalle.getSubTotal());
                    dto.setCantidad(detalle.getCantidad());
                    dto.setCategoria(detalle.getProductoTienda().getProducto().getCategoria().getNombre());
                    dto.setUrlProducto(detalle.getProductoTienda().getUrlProducto());
                    dto.setDetalles(mapToDetalleAtributo(detalle.getProductoTienda().getProducto().getAtributos()));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public static TiendaInfoResponse mapToTiendaInfo(Tienda tienda) {
        return new TiendaInfoResponse(
                tienda.getIdUsuario(),
                tienda.getNombre(),
                tienda.getEmail(),
                tienda.isActivo(),
                tienda.getFechaAfiliacion(),
                tienda.isVerificado(),
                tienda.getSuscripciones().size(),
                tienda.getEtiquetas().size(),
                tienda.getProductos().size());
    }

    public static TiendaDetallesResponse mapToTiendaDetalles(Tienda tienda) {

        Suscripcion lastSuscripcion = tienda.getSuscripciones().isEmpty()
                ? null
                : tienda.getSuscripciones().getLast();

        TiendaSuscripcionActualInfoResponse suscripcionResponse = (lastSuscripcion != null)
                ? mapToTiendaSuscripcionActualInfo(lastSuscripcion)
                : null;

        return new TiendaDetallesResponse(
                tienda.getIdUsuario(),
                tienda.getNombre(),
                tienda.getEmail(),
                tienda.isActivo(),
                tienda.getFechaAfiliacion(),
                tienda.isVerificado(),
                tienda.getTelefono(),
                tienda.getDireccion(),
                tienda.getUrlPagina(),
                tienda.getTiendaAPI(),
                suscripcionResponse,
                tienda.getEtiquetas(),
                mapToProductoTiendaInfo(tienda.getProductos()));
    }

    public static TiendaInfoDetalleResponse mapToTiendaInfoDetalle(Tienda tienda) {
        return new TiendaInfoDetalleResponse(
                tienda.getIdUsuario(),
                tienda.getNombre(),
                tienda.getEmail(),
                tienda.isActivo(),
                tienda.getDescripcion(),
                tienda.getFechaAfiliacion(),
                tienda.getTelefono(),
                tienda.getDireccion(),
                tienda.getUrlPagina(),
                tienda.getEtiquetas(),
                tienda.getProductos().size(),
                tienda.getLogo());
    }

    public static List<ProductoTiendaInfoResponse> mapToProductoTiendaInfo(List<ProductoTienda> productosTienda) {
        return productosTienda.stream()
                .map(Mapper::mapToProductoTiendaInfo)
                .collect(Collectors.toList());
    }

    public static ProductoTiendaInfoResponse mapToProductoTiendaInfo(ProductoTienda productoTienda) {
        return new ProductoTiendaInfoResponse(
                productoTienda.getIdProductoTienda(),
                productoTienda.getProducto().getNombre(),
                productoTienda.getProducto().getCategoria().getNombre(),
                productoTienda.getPrecio(),
                productoTienda.getStock(),
                productoTienda.getUrlProducto(),
                productoTienda.getUrlImagen(),
                productoTienda.getHabilitado());
    }

    public static TiendaSuscripcionActualInfoResponse mapToTiendaSuscripcionActualInfo(Suscripcion suscripcion) {
        return new TiendaSuscripcionActualInfoResponse(
                suscripcion.getFechaInicio(),
                suscripcion.getFechaFin(),
                suscripcion.getEstado().name(),
                suscripcion.getPlan().getNombre());
    }
}
