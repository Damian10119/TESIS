package com.bazarchinita.backend.reportes.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bazarchinita.backend.reportes.dto.InventarioActualResponse;
import com.bazarchinita.backend.reportes.dto.MovimientoInventarioResponse;
import com.bazarchinita.backend.reportes.dto.PagoVentaResponse;
import com.bazarchinita.backend.reportes.dto.ResumenVentaDiariaResponse;
import com.bazarchinita.backend.reportes.dto.StockBajoResponse;

@Service
public class ReporteService {

    private final JdbcTemplate jdbcTemplate;

    public ReporteService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<ResumenVentaDiariaResponse> obtenerResumenVentasDiarias() {
        String sql = """
                SELECT
                    fecha,
                    cantidad_ventas,
                    subtotal_vendido,
                    descuento_total,
                    iva_total,
                    total_vendido
                FROM vista_resumen_ventas_diarias
                ORDER BY fecha DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ResumenVentaDiariaResponse(
                rs.getDate("fecha").toLocalDate(),
                rs.getInt("cantidad_ventas"),
                rs.getBigDecimal("subtotal_vendido"),
                rs.getBigDecimal("descuento_total"),
                rs.getBigDecimal("iva_total"),
                rs.getBigDecimal("total_vendido")
        ));
    }

    @Transactional(readOnly = true)
    public List<InventarioActualResponse> obtenerInventarioActual() {
        String sql = """
                SELECT
                    id_producto,
                    codigo_producto,
                    nombre_producto,
                    nombre_categoria,
                    precio_compra,
                    precio_venta,
                    stock_actual,
                    stock_minimo,
                    estado_stock,
                    aplica_iva,
                    porcentaje_iva,
                    estado
                FROM vista_inventario_actual
                ORDER BY nombre_producto ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new InventarioActualResponse(
                rs.getInt("id_producto"),
                rs.getString("codigo_producto"),
                rs.getString("nombre_producto"),
                rs.getString("nombre_categoria"),
                rs.getBigDecimal("precio_compra"),
                rs.getBigDecimal("precio_venta"),
                rs.getInt("stock_actual"),
                rs.getInt("stock_minimo"),
                rs.getString("estado_stock"),
                rs.getBoolean("aplica_iva"),
                rs.getBigDecimal("porcentaje_iva"),
                rs.getBoolean("estado")
        ));
    }

    @Transactional(readOnly = true)
    public List<StockBajoResponse> obtenerStockBajo() {
        String sql = """
                SELECT
                    id_producto,
                    codigo_producto,
                    nombre_producto,
                    nombre_categoria,
                    stock_actual,
                    stock_minimo,
                    estado_stock
                FROM vista_productos_stock_bajo
                ORDER BY nombre_producto ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new StockBajoResponse(
                rs.getInt("id_producto"),
                rs.getString("codigo_producto"),
                rs.getString("nombre_producto"),
                rs.getString("nombre_categoria"),
                rs.getInt("stock_actual"),
                rs.getInt("stock_minimo"),
                rs.getString("estado_stock")
        ));
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> obtenerMovimientosInventario() {
        String sql = """
                SELECT
                    id_movimiento,
                    fecha_movimiento,
                    codigo_producto,
                    nombre_producto,
                    nombre_categoria,
                    usuario_registra,
                    id_venta,
                    tipo_movimiento,
                    cantidad,
                    stock_anterior,
                    stock_nuevo,
                    motivo
                FROM vista_movimientos_inventario
                ORDER BY fecha_movimiento DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new MovimientoInventarioResponse(
                rs.getInt("id_movimiento"),
                rs.getTimestamp("fecha_movimiento").toLocalDateTime(),
                rs.getString("codigo_producto"),
                rs.getString("nombre_producto"),
                rs.getString("nombre_categoria"),
                rs.getString("usuario_registra"),
                rs.getObject("id_venta", Integer.class),
                rs.getString("tipo_movimiento"),
                rs.getInt("cantidad"),
                rs.getInt("stock_anterior"),
                rs.getInt("stock_nuevo"),
                rs.getString("motivo")
        ));
    }

    @Transactional(readOnly = true)
    public List<PagoVentaResponse> obtenerPagosVentas() {
        String sql = """
                SELECT
                    id_pago,
                    id_venta,
                    fecha_venta,
                    cliente,
                    nombre_metodo,
                    monto,
                    referencia_pago,
                    estado_pago,
                    fecha_pago,
                    observacion
                FROM vista_pagos_ventas
                ORDER BY fecha_pago DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new PagoVentaResponse(
                rs.getInt("id_pago"),
                rs.getInt("id_venta"),
                rs.getTimestamp("fecha_venta").toLocalDateTime(),
                rs.getString("cliente"),
                rs.getString("nombre_metodo"),
                rs.getBigDecimal("monto"),
                rs.getString("referencia_pago"),
                rs.getString("estado_pago"),
                rs.getTimestamp("fecha_pago").toLocalDateTime(),
                rs.getString("observacion")
        ));
    }
}
