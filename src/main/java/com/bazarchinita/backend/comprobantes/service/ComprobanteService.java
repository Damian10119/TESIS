package com.bazarchinita.backend.comprobantes.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.comprobantes.dto.ComprobanteResponse;

@Service
public class ComprobanteService {

    private final JdbcTemplate jdbcTemplate;

    public ComprobanteService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<ComprobanteResponse> listarComprobantes() {
        String sql = """
                SELECT
                    id_comprobante,
                    id_venta,
                    tipo_comprobante,
                    numero_comprobante,
                    fecha_emision,
                    cliente,
                    identificacion_cliente,
                    subtotal,
                    iva,
                    total,
                    estado_comprobante,
                    clave_acceso,
                    estado_sri,
                    numero_autorizacion,
                    fecha_autorizacion
                FROM vista_comprobantes_emitidos
                ORDER BY fecha_emision DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ComprobanteResponse(
                rs.getInt("id_comprobante"),
                rs.getInt("id_venta"),
                rs.getString("tipo_comprobante"),
                rs.getString("numero_comprobante"),
                convertirTimestamp(rs.getTimestamp("fecha_emision")),
                rs.getString("cliente"),
                rs.getString("identificacion_cliente"),
                rs.getBigDecimal("subtotal"),
                rs.getBigDecimal("iva"),
                rs.getBigDecimal("total"),
                rs.getString("estado_comprobante"),
                rs.getString("clave_acceso"),
                rs.getString("estado_sri"),
                rs.getString("numero_autorizacion"),
                convertirTimestamp(rs.getTimestamp("fecha_autorizacion"))
        ));
    }

    @Transactional(readOnly = true)
    public ComprobanteResponse buscarPorId(Integer idComprobante) {
        String sql = """
                SELECT
                    id_comprobante,
                    id_venta,
                    tipo_comprobante,
                    numero_comprobante,
                    fecha_emision,
                    cliente,
                    identificacion_cliente,
                    subtotal,
                    iva,
                    total,
                    estado_comprobante,
                    clave_acceso,
                    estado_sri,
                    numero_autorizacion,
                    fecha_autorizacion
                FROM vista_comprobantes_emitidos
                WHERE id_comprobante = ?
                """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new ComprobanteResponse(
                            rs.getInt("id_comprobante"),
                            rs.getInt("id_venta"),
                            rs.getString("tipo_comprobante"),
                            rs.getString("numero_comprobante"),
                            convertirTimestamp(rs.getTimestamp("fecha_emision")),
                            rs.getString("cliente"),
                            rs.getString("identificacion_cliente"),
                            rs.getBigDecimal("subtotal"),
                            rs.getBigDecimal("iva"),
                            rs.getBigDecimal("total"),
                            rs.getString("estado_comprobante"),
                            rs.getString("clave_acceso"),
                            rs.getString("estado_sri"),
                            rs.getString("numero_autorizacion"),
                            convertirTimestamp(rs.getTimestamp("fecha_autorizacion"))
                    ),
                    idComprobante
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Comprobante no encontrado"
            );
        }
    }

    @Transactional(readOnly = true)
    public ComprobanteResponse buscarPorVenta(Integer idVenta) {
        String sql = """
                SELECT
                    id_comprobante,
                    id_venta,
                    tipo_comprobante,
                    numero_comprobante,
                    fecha_emision,
                    cliente,
                    identificacion_cliente,
                    subtotal,
                    iva,
                    total,
                    estado_comprobante,
                    clave_acceso,
                    estado_sri,
                    numero_autorizacion,
                    fecha_autorizacion
                FROM vista_comprobantes_emitidos
                WHERE id_venta = ?
                """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new ComprobanteResponse(
                            rs.getInt("id_comprobante"),
                            rs.getInt("id_venta"),
                            rs.getString("tipo_comprobante"),
                            rs.getString("numero_comprobante"),
                            convertirTimestamp(rs.getTimestamp("fecha_emision")),
                            rs.getString("cliente"),
                            rs.getString("identificacion_cliente"),
                            rs.getBigDecimal("subtotal"),
                            rs.getBigDecimal("iva"),
                            rs.getBigDecimal("total"),
                            rs.getString("estado_comprobante"),
                            rs.getString("clave_acceso"),
                            rs.getString("estado_sri"),
                            rs.getString("numero_autorizacion"),
                            convertirTimestamp(rs.getTimestamp("fecha_autorizacion"))
                    ),
                    idVenta
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe comprobante para la venta indicada"
            );
        }
    }

    @Transactional(readOnly = true)
    public ComprobanteResponse buscarPorNumero(String numeroComprobante) {
        String sql = """
                SELECT
                    id_comprobante,
                    id_venta,
                    tipo_comprobante,
                    numero_comprobante,
                    fecha_emision,
                    cliente,
                    identificacion_cliente,
                    subtotal,
                    iva,
                    total,
                    estado_comprobante,
                    clave_acceso,
                    estado_sri,
                    numero_autorizacion,
                    fecha_autorizacion
                FROM vista_comprobantes_emitidos
                WHERE numero_comprobante = ?
                """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new ComprobanteResponse(
                            rs.getInt("id_comprobante"),
                            rs.getInt("id_venta"),
                            rs.getString("tipo_comprobante"),
                            rs.getString("numero_comprobante"),
                            convertirTimestamp(rs.getTimestamp("fecha_emision")),
                            rs.getString("cliente"),
                            rs.getString("identificacion_cliente"),
                            rs.getBigDecimal("subtotal"),
                            rs.getBigDecimal("iva"),
                            rs.getBigDecimal("total"),
                            rs.getString("estado_comprobante"),
                            rs.getString("clave_acceso"),
                            rs.getString("estado_sri"),
                            rs.getString("numero_autorizacion"),
                            convertirTimestamp(rs.getTimestamp("fecha_autorizacion"))
                    ),
                    numeroComprobante.trim()
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Comprobante no encontrado"
            );
        }
    }

    private LocalDateTime convertirTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime();
    }
}
