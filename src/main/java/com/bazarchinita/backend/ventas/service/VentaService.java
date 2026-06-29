package com.bazarchinita.backend.ventas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.usuarios.entity.Usuario;
import com.bazarchinita.backend.usuarios.repository.UsuarioRepository;
import com.bazarchinita.backend.ventas.dto.AnularVentaRequest;
import com.bazarchinita.backend.ventas.dto.AnularVentaResponse;
import com.bazarchinita.backend.ventas.dto.DetalleVentaConsultaResponse;
import com.bazarchinita.backend.ventas.dto.VentaConsultaResponse;
import com.bazarchinita.backend.ventas.dto.VentaDetalleRequest;
import com.bazarchinita.backend.ventas.dto.VentaPagoRequest;
import com.bazarchinita.backend.ventas.dto.VentaRequest;
import com.bazarchinita.backend.ventas.dto.VentaResultadoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VentaService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final UsuarioRepository usuarioRepository;

    public VentaService(
            JdbcTemplate jdbcTemplate,
            ObjectMapper objectMapper,
            UsuarioRepository usuarioRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public VentaResultadoResponse registrarVenta(VentaRequest request) {
        Usuario usuarioActual = obtenerUsuarioActual();

        String detallesJson = convertirDetallesAJson(request.getDetalles());
        String pagosJson = convertirPagosAJson(request.getPagos());

        String tipoComprobante = normalizarTipoComprobante(request.getTipoComprobante());
        String observacion = limpiarTextoONull(request.getObservacion());

        String sql = """
                SELECT *
                FROM registrar_venta_completa_pagos_mixtos(
                    ?,
                    ?,
                    CAST(? AS jsonb),
                    CAST(? AS jsonb),
                    ?,
                    ?
                )
                """;

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new VentaResultadoResponse(
                        rs.getInt("id_venta_generada"),
                        rs.getString("numero_comprobante_generado"),
                        rs.getBigDecimal("total_venta"),
                        rs.getBigDecimal("total_pagado")
                ),
                request.getIdCliente(),
                usuarioActual.getIdUsuario(),
                detallesJson,
                pagosJson,
                tipoComprobante,
                observacion
        );
    }

    @Transactional(readOnly = true)
    public List<VentaConsultaResponse> listarVentas() {
        String sql = """
                SELECT
                    id_venta,
                    fecha_venta,
                    cliente,
                    identificacion_cliente,
                    es_consumidor_final,
                    usuario_registra,
                    subtotal,
                    descuento_total,
                    iva_total,
                    total,
                    total_pagado,
                    estado_venta,
                    tipo_comprobante,
                    numero_comprobante,
                    estado_comprobante
                FROM vista_ventas_completas
                ORDER BY fecha_venta DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new VentaConsultaResponse(
                rs.getInt("id_venta"),
                rs.getTimestamp("fecha_venta").toLocalDateTime(),
                rs.getString("cliente"),
                rs.getString("identificacion_cliente"),
                rs.getBoolean("es_consumidor_final"),
                rs.getString("usuario_registra"),
                rs.getBigDecimal("subtotal"),
                rs.getBigDecimal("descuento_total"),
                rs.getBigDecimal("iva_total"),
                rs.getBigDecimal("total"),
                rs.getBigDecimal("total_pagado"),
                rs.getString("estado_venta"),
                rs.getString("tipo_comprobante"),
                rs.getString("numero_comprobante"),
                rs.getString("estado_comprobante")
        ));
    }

    @Transactional(readOnly = true)
    public List<DetalleVentaConsultaResponse> obtenerDetalleVenta(Integer idVenta) {
        String sql = """
                SELECT
                    id_venta,
                    fecha_venta,
                    cliente,
                    usuario_registra,
                    codigo_producto,
                    nombre_producto,
                    nombre_categoria,
                    cantidad,
                    precio_unitario,
                    descuento,
                    subtotal,
                    iva,
                    total_linea,
                    estado_venta
                FROM vista_detalle_ventas
                WHERE id_venta = ?
                ORDER BY nombre_producto ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new DetalleVentaConsultaResponse(
                rs.getInt("id_venta"),
                rs.getTimestamp("fecha_venta").toLocalDateTime(),
                rs.getString("cliente"),
                rs.getString("usuario_registra"),
                rs.getString("codigo_producto"),
                rs.getString("nombre_producto"),
                rs.getString("nombre_categoria"),
                rs.getInt("cantidad"),
                rs.getBigDecimal("precio_unitario"),
                rs.getBigDecimal("descuento"),
                rs.getBigDecimal("subtotal"),
                rs.getBigDecimal("iva"),
                rs.getBigDecimal("total_linea"),
                rs.getString("estado_venta")
        ), idVenta);
    }

    @Transactional
    public AnularVentaResponse anularVenta(Integer idVenta, AnularVentaRequest request) {
        Usuario usuarioActual = obtenerUsuarioActual();

        String motivo = request.getMotivo() == null || request.getMotivo().trim().isEmpty()
                ? "Anulación de venta solicitada desde el backend."
                : request.getMotivo().trim();

        String sql = """
                SELECT *
                FROM anular_venta(?, ?, ?)
                """;

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new AnularVentaResponse(
                        rs.getInt("id_venta_anulada"),
                        rs.getString("estado_final"),
                        rs.getString("mensaje")
                ),
                idVenta,
                usuarioActual.getIdUsuario(),
                motivo
        );
    }

    private Usuario obtenerUsuarioActual() {
        String nombreUsuario = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findByNombreUsuarioIgnoreCase(nombreUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "No se pudo identificar el usuario autenticado"
                ));
    }

    private String convertirDetallesAJson(List<VentaDetalleRequest> detalles) {
        List<Map<String, Object>> detallesPostgres = new ArrayList<>();

        for (VentaDetalleRequest detalle : detalles) {
            detallesPostgres.add(Map.of(
                    "id_producto", detalle.getIdProducto(),
                    "cantidad", detalle.getCantidad(),
                    "descuento", detalle.getDescuento() != null ? detalle.getDescuento() : BigDecimal.ZERO
            ));
        }

        return convertirAJson(detallesPostgres);
    }

    private String convertirPagosAJson(List<VentaPagoRequest> pagos) {
        List<Map<String, Object>> pagosPostgres = new ArrayList<>();

        for (VentaPagoRequest pago : pagos) {
            pagosPostgres.add(Map.of(
                    "id_metodo_pago", pago.getIdMetodoPago(),
                    "monto", pago.getMonto(),
                    "referencia_pago", pago.getReferenciaPago() != null ? pago.getReferenciaPago() : ""
            ));
        }

        return convertirAJson(pagosPostgres);
    }

    private String convertirAJson(Object objeto) {
        try {
            return objectMapper.writeValueAsString(objeto);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pudo procesar la información de la venta"
            );
        }
    }

    private String normalizarTipoComprobante(String tipoComprobante) {
        if (tipoComprobante == null || tipoComprobante.trim().isEmpty()) {
            return "COMPROBANTE_INTERNO";
        }

        String tipo = tipoComprobante.trim().toUpperCase();

        if (!tipo.equals("FACTURA")
                && !tipo.equals("NOTA_VENTA")
                && !tipo.equals("COMPROBANTE_INTERNO")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de comprobante no válido. Use FACTURA, NOTA_VENTA o COMPROBANTE_INTERNO"
            );
        }

        return tipo;
    }

    private String limpiarTextoONull(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }

        return texto.trim();
    }
}
