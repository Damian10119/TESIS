package com.bazarchinita.backend.productos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bazarchinita.backend.productos.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByEstadoTrueOrderByNombreProductoAsc();

    List<Producto> findByEstadoTrueAndNombreProductoContainingIgnoreCaseOrderByNombreProductoAsc(String nombreProducto);

    Optional<Producto> findByCodigoProductoIgnoreCase(String codigoProducto);

    boolean existsByCodigoProductoIgnoreCase(String codigoProducto);

    @Query("""
            SELECT p
            FROM Producto p
            WHERE p.estado = true
              AND p.stockActual <= p.stockMinimo
            ORDER BY p.nombreProducto ASC
            """)
    List<Producto> findProductosConStockBajo();
}