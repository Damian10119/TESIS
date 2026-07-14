package com.bazarchinita.backend.facturacionelectronica.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.facturacionelectronica.dto.ActualizarEstadoFacturaRequest;
import com.bazarchinita.backend.facturacionelectronica.dto.FacturaElectronicaResponse;

@Service
public class FacturaElectronicaService {

    private final JdbcTemplate jdbcTemplate;

    public FacturaElectronicaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<FacturaElectronicaResponse> listar() {
        String sql = consultaBase() + """
                ORDER BY co.fecha_emision DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapearFactura(rs));
    }

    @Transactional(readOnly = true)
    public FacturaElectronicaResponse buscarPorId(Integer idFactura) {
        String sql = consultaBase() + """
                WHERE fe.id_factura_electronica = ?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapearFactura(rs), idFactura);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura electrónica no encontrada");
        }
    }

    @Transactional(readOnly = true)
    public FacturaElectronicaResponse buscarPorVenta(Integer idVenta) {
        String sql = consultaBase() + """
                WHERE co.id_venta = ?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapearFactura(rs), idVenta);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe factura electrónica para la venta indicada");
        }
    }

    @Transactional(readOnly = true)
    public FacturaElectronicaResponse buscarPorComprobante(Integer idComprobante) {
        String sql = consultaBase() + """
                WHERE fe.id_comprobante = ?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapearFactura(rs), idComprobante);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe factura electrónica para el comprobante indicado");
        }
    }

    @Transactional
    public FacturaElectronicaResponse generarDesdeVenta(Integer idVenta) {
        Map<String, Object> comprobante = obtenerComprobantePorVenta(idVenta);

        Integer idComprobante = (Integer) comprobante.get("id_comprobante");
        String tipoComprobante = (String) comprobante.get("tipo_comprobante");
        String estadoComprobante = (String) comprobante.get("estado_comprobante");

        if (!"FACTURA".equals(tipoComprobante)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se puede generar factura electrónica para comprobantes de tipo FACTURA"
            );
        }

        if ("ANULADO".equals(estadoComprobante)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede generar factura electrónica para un comprobante anulado"
            );
        }

        if (existeFacturaParaComprobante(idComprobante)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe una factura electrónica para esta venta"
            );
        }

        String claveAcceso = generarClaveAccesoSimulada(comprobante);

        String insertSql = """
                INSERT INTO facturas_electronicas (
                    id_comprobante,
                    clave_acceso,
                    estado_sri,
                    mensaje_sri,
                    ruta_xml,
                    ruta_pdf
                )
                VALUES (?, ?, 'PENDIENTE', ?, ?, ?)
                RETURNING id_factura_electronica
                """;

        Integer idFactura = jdbcTemplate.queryForObject(
                insertSql,
                Integer.class,
                idComprobante,
                claveAcceso,
                "Factura electrónica generada internamente. Pendiente de envío/autorización.",
                "facturas/xml/" + claveAcceso + ".xml",
                "facturas/pdf/" + claveAcceso + ".pdf"
        );

        jdbcTemplate.update(
                "UPDATE comprobantes SET clave_acceso = ? WHERE id_comprobante = ?",
                claveAcceso,
                idComprobante
        );

        return buscarPorId(idFactura);
    }

    @Transactional
    public FacturaElectronicaResponse marcarRecibida(
            Integer idFactura,
            ActualizarEstadoFacturaRequest request
    ) {
        actualizarEstadoSri(
                idFactura,
                "RECIBIDA",
                null,
                null,
                limpiarTextoONull(request.getMensajeSri()) != null
                        ? limpiarTextoONull(request.getMensajeSri())
                        : "Factura recibida para proceso de autorización.",
                request
        );

        return buscarPorId(idFactura);
    }

    @Transactional
    public FacturaElectronicaResponse marcarAutorizada(
            Integer idFactura,
            ActualizarEstadoFacturaRequest request
    ) {
        String numeroAutorizacion = limpiarTextoONull(request.getNumeroAutorizacion());

        if (numeroAutorizacion == null) {
            numeroAutorizacion = generarNumeroAutorizacionSimulado();
        }

        actualizarEstadoSri(
                idFactura,
                "AUTORIZADA",
                numeroAutorizacion,
                LocalDateTime.now(),
                limpiarTextoONull(request.getMensajeSri()) != null
                        ? limpiarTextoONull(request.getMensajeSri())
                        : "Factura autorizada correctamente.",
                request
        );

        Integer idComprobante = obtenerIdComprobantePorFactura(idFactura);

        jdbcTemplate.update(
                "UPDATE comprobantes SET estado_comprobante = 'AUTORIZADO' WHERE id_comprobante = ?",
                idComprobante
        );

        return buscarPorId(idFactura);
    }

    @Transactional
    public FacturaElectronicaResponse marcarRechazada(
            Integer idFactura,
            ActualizarEstadoFacturaRequest request
    ) {
        actualizarEstadoSri(
                idFactura,
                "RECHAZADA",
                null,
                null,
                limpiarTextoONull(request.getMensajeSri()) != null
                        ? limpiarTextoONull(request.getMensajeSri())
                        : "Factura rechazada durante el proceso de validación.",
                request
        );

        Integer idComprobante = obtenerIdComprobantePorFactura(idFactura);

        jdbcTemplate.update(
                "UPDATE comprobantes SET estado_comprobante = 'RECHAZADO' WHERE id_comprobante = ?",
                idComprobante
        );

        return buscarPorId(idFactura);
    }

    private void actualizarEstadoSri(
            Integer idFactura,
            String estadoSri,
            String numeroAutorizacion,
            LocalDateTime fechaAutorizacion,
            String mensajeSri,
            ActualizarEstadoFacturaRequest request
    ) {
        String sql = """
                UPDATE facturas_electronicas
                SET estado_sri = ?,
                    numero_autorizacion = ?,
                    fecha_autorizacion = ?,
                    mensaje_sri = ?,
                    ruta_xml = COALESCE(?, ruta_xml),
                    ruta_pdf = COALESCE(?, ruta_pdf)
                WHERE id_factura_electronica = ?
                """;

        int filas = jdbcTemplate.update(
                sql,
                estadoSri,
                numeroAutorizacion,
                fechaAutorizacion,
                mensajeSri,
                limpiarTextoONull(request.getRutaXml()),
                limpiarTextoONull(request.getRutaPdf()),
                idFactura
        );

        if (filas == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura electrónica no encontrada");
        }
    }

    private Map<String, Object> obtenerComprobantePorVenta(Integer idVenta) {
        String sql = """
                SELECT
                    co.id_comprobante,
                    co.id_venta,
                    co.tipo_comprobante,
                    co.numero_comprobante,
                    co.fecha_emision,
                    co.total,
                    co.estado_comprobante,
                    cn.ruc,
                    cn.ambiente_facturacion
                FROM comprobantes co
                CROSS JOIN configuracion_negocio cn
                WHERE co.id_venta = ?
                  AND cn.estado = true
                ORDER BY cn.id_configuracion ASC
                LIMIT 1
                """;

        try {
            return jdbcTemplate.queryForMap(sql, idVenta);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe comprobante para la venta indicada");
        }
    }

    private boolean existeFacturaParaComprobante(Integer idComprobante) {
        String sql = """
                SELECT COUNT(*)
                FROM facturas_electronicas
                WHERE id_comprobante = ?
                """;

        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, idComprobante);

        return total != null && total > 0;
    }

    private Integer obtenerIdComprobantePorFactura(Integer idFactura) {
        String sql = """
                SELECT id_comprobante
                FROM facturas_electronicas
                WHERE id_factura_electronica = ?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, idFactura);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura electrónica no encontrada");
        }
    }

    private String generarClaveAccesoSimulada(Map<String, Object> comprobante) {
        Timestamp fechaEmisionTimestamp = (Timestamp) comprobante.get("fecha_emision");
        LocalDateTime fechaEmision = fechaEmisionTimestamp.toLocalDateTime();

        String fecha = fechaEmision.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String codigoDocumento = "01";

        String ruc = comprobante.get("ruc") != null
                ? comprobante.get("ruc").toString()
                : "9999999999999";

        if (ruc.length() != 13 || !ruc.matches("\\d{13}")) {
            ruc = "9999999999999";
        }

        String ambiente = "PRODUCCION".equals(comprobante.get("ambiente_facturacion"))
                ? "2"
                : "1";

        String serie = "001001";

        String numeroComprobante = comprobante.get("numero_comprobante").toString();
        String secuencial = extraerSecuencial(numeroComprobante);

        String codigoNumerico = String.format("%08d", Math.abs(numeroComprobante.hashCode()) % 100000000);
        String tipoEmision = "1";

        String base = fecha
                + codigoDocumento
                + ruc
                + ambiente
                + serie
                + secuencial
                + codigoNumerico
                + tipoEmision;

        String digitoVerificador = calcularDigitoVerificadorModulo11(base);

        return base + digitoVerificador;
    }

    private String extraerSecuencial(String numeroComprobante) {
        String soloNumeros = numeroComprobante.replaceAll("\\D", "");

        if (soloNumeros.isEmpty()) {
            return "000000001";
        }

        if (soloNumeros.length() > 9) {
            soloNumeros = soloNumeros.substring(soloNumeros.length() - 9);
        }

        return String.format("%09d", Integer.parseInt(soloNumeros));
    }

    private String calcularDigitoVerificadorModulo11(String base) {
        int[] factores = {2, 3, 4, 5, 6, 7};
        int factorIndex = 0;
        int suma = 0;

        for (int i = base.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(base.charAt(i));
            suma += digito * factores[factorIndex];

            factorIndex++;
            if (factorIndex == factores.length) {
                factorIndex = 0;
            }
        }

        int modulo = suma % 11;
        int resultado = 11 - modulo;

        if (resultado == 11) {
            return "0";
        }

        if (resultado == 10) {
            return "1";
        }

        return String.valueOf(resultado);
    }

    private String generarNumeroAutorizacionSimulado() {
        return "AUT-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private String consultaBase() {
        return """
                SELECT
                    fe.id_factura_electronica,
                    fe.id_comprobante,
                    co.id_venta,
                    co.tipo_comprobante,
                    co.numero_comprobante,
                    co.fecha_emision,
                    c.nombres AS cliente,
                    c.identificacion AS identificacion_cliente,
                    co.total,
                    fe.clave_acceso,
                    fe.numero_autorizacion,
                    fe.fecha_autorizacion,
                    fe.estado_sri,
                    fe.mensaje_sri,
                    fe.ruta_xml,
                    fe.ruta_pdf
                FROM facturas_electronicas fe
                INNER JOIN comprobantes co
                    ON fe.id_comprobante = co.id_comprobante
                INNER JOIN ventas v
                    ON co.id_venta = v.id_venta
                INNER JOIN clientes c
                    ON v.id_cliente = c.id_cliente
                """;
    }

    private FacturaElectronicaResponse mapearFactura(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new FacturaElectronicaResponse(
                rs.getInt("id_factura_electronica"),
                rs.getInt("id_comprobante"),
                rs.getInt("id_venta"),
                rs.getString("tipo_comprobante"),
                rs.getString("numero_comprobante"),
                convertirTimestamp(rs.getTimestamp("fecha_emision")),
                rs.getString("cliente"),
                rs.getString("identificacion_cliente"),
                rs.getBigDecimal("total"),
                rs.getString("clave_acceso"),
                rs.getString("numero_autorizacion"),
                convertirTimestamp(rs.getTimestamp("fecha_autorizacion")),
                rs.getString("estado_sri"),
                rs.getString("mensaje_sri"),
                rs.getString("ruta_xml"),
                rs.getString("ruta_pdf")
        );
    }

    private LocalDateTime convertirTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime();
    }

    private String limpiarTextoONull(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }

        return texto.trim();
    }
}