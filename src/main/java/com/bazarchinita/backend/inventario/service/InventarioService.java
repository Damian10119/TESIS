package com.bazarchinita.backend.inventario.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.inventario.dto.AjustarStockRequest;
import com.bazarchinita.backend.inventario.dto.AjustarStockResponse;
import com.bazarchinita.backend.inventario.dto.MovimientoInventarioResponse;
import com.bazarchinita.backend.usuarios.entity.Usuario;
import com.bazarchinita.backend.usuarios.repository.UsuarioRepository;

@Service
public class InventarioService {

    private final JdbcTemplate jdbcTemplate;
    private final UsuarioRepository usuarioRepository;

    public InventarioService(
            JdbcTemplate jdbcTemplate,
            UsuarioRepository usuarioRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AjustarStockResponse ajustarStock(AjustarStockRequest request) {
        Usuario usuarioActual = obtenerUsuarioActual();

        String tipoMovimiento = normalizarTipoMovimiento(request.getTipoMovimiento());
        String motivo = limpiarMotivo(request.getMotivo());

        String sql = """
                SELECT *
                FROM ajustar_stock_producto(
                    ?,
                    ?,
                    ?,
                    ?,
                    ?
                )
                """;

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new AjustarStockResponse(
                        rs.getInt("id_producto_actualizado"),
                        rs.getInt("stock_anterior_resultado"),
                        rs.getInt("stock_nuevo_resultado"),
                        rs.getString("tipo_movimiento_resultado"),
                        rs.getString("mensaje")
                ),
                request.getIdProducto(),
                usuarioActual.getIdUsuario(),
                tipoMovimiento,
                request.getCantidad(),
                motivo
        );
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> listarMovimientos() {
        String sql = """
                SELECT
                    mi.id_movimiento,
                    p.id_producto,
                    mi.fecha_movimiento,
                    p.codigo_producto,
                    p.nombre_producto,
                    cat.nombre_categoria,
                    u.nombre AS usuario_registra,
                    mi.id_venta,
                    mi.tipo_movimiento,
                    mi.cantidad,
                    mi.stock_anterior,
                    mi.stock_nuevo,
                    mi.motivo
                FROM movimientos_inventario mi
                INNER JOIN productos p
                    ON mi.id_producto = p.id_producto
                INNER JOIN categorias cat
                    ON p.id_categoria = cat.id_categoria
                INNER JOIN usuarios u
                    ON mi.id_usuario = u.id_usuario
                ORDER BY mi.fecha_movimiento DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapearMovimiento(rs));
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> listarMovimientosPorProducto(Integer idProducto) {
        String sql = """
                SELECT
                    mi.id_movimiento,
                    p.id_producto,
                    mi.fecha_movimiento,
                    p.codigo_producto,
                    p.nombre_producto,
                    cat.nombre_categoria,
                    u.nombre AS usuario_registra,
                    mi.id_venta,
                    mi.tipo_movimiento,
                    mi.cantidad,
                    mi.stock_anterior,
                    mi.stock_nuevo,
                    mi.motivo
                FROM movimientos_inventario mi
                INNER JOIN productos p
                    ON mi.id_producto = p.id_producto
                INNER JOIN categorias cat
                    ON p.id_categoria = cat.id_categoria
                INNER JOIN usuarios u
                    ON mi.id_usuario = u.id_usuario
                WHERE p.id_producto = ?
                ORDER BY mi.fecha_movimiento DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapearMovimiento(rs), idProducto);
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

    private MovimientoInventarioResponse mapearMovimiento(java.sql.ResultSet rs)
            throws java.sql.SQLException {
        return new MovimientoInventarioResponse(
                rs.getInt("id_movimiento"),
                rs.getInt("id_producto"),
                convertirTimestamp(rs.getTimestamp("fecha_movimiento")),
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
        );
    }

    private String normalizarTipoMovimiento(String tipoMovimiento) {
        String tipo = tipoMovimiento.trim().toUpperCase();

        if (!tipo.equals("ENTRADA")
                && !tipo.equals("SALIDA")
                && !tipo.equals("AJUSTE")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de movimiento no válido. Use ENTRADA, SALIDA o AJUSTE"
            );
        }

        return tipo;
    }

    private String limpiarMotivo(String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            return "Ajuste manual de stock desde el backend.";
        }

        return motivo.trim();
    }

    private LocalDateTime convertirTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime();
    }
}
