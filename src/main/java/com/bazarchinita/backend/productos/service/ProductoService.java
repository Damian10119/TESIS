package com.bazarchinita.backend.productos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import com.bazarchinita.backend.categorias.entity.Categoria;
import com.bazarchinita.backend.categorias.repository.CategoriaRepository;
import com.bazarchinita.backend.productos.dto.ProductoRequest;
import com.bazarchinita.backend.productos.dto.ProductoResponse;
import com.bazarchinita.backend.productos.entity.Producto;
import com.bazarchinita.backend.productos.repository.ProductoRepository;


@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarActivos() {
        return productoRepository.findByEstadoTrueOrderByNombreProductoAsc()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"
                ));

        return convertirAResponse(producto);
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorCodigo(String codigo) {
        Producto producto = productoRepository.findByCodigoProductoIgnoreCase(codigo.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"
                ));

        return convertirAResponse(producto);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorNombre(String nombre) {
        return productoRepository
                .findByEstadoTrueAndNombreProductoContainingIgnoreCaseOrderByNombreProductoAsc(nombre.trim())
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarStockBajo() {
        return productoRepository.findProductosConStockBajo()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        String codigoLimpio = request.getCodigoProducto().trim();
        String nombreLimpio = request.getNombreProducto().trim();

        if (productoRepository.existsByCodigoProductoIgnoreCase(codigoLimpio)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe un producto con ese código"
            );
        }

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría no encontrada"
                ));

        if (Boolean.FALSE.equals(categoria.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede registrar un producto en una categoría inactiva"
            );
        }

        Producto producto = new Producto();
        producto.setCategoria(categoria);
        producto.setCodigoProducto(codigoLimpio);
        producto.setNombreProducto(nombreLimpio);
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioCompra(valorONumeroCero(request.getPrecioCompra()));
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setStockActual(valorOEnteroCero(request.getStockActual()));
        producto.setStockMinimo(valorOEnteroCero(request.getStockMinimo()));

        Boolean aplicaIva = request.getAplicaIva() != null ? request.getAplicaIva() : true;
        producto.setAplicaIva(aplicaIva);

        if (Boolean.TRUE.equals(aplicaIva)) {
            producto.setPorcentajeIva(
                    request.getPorcentajeIva() != null
                            ? request.getPorcentajeIva()
                            : new BigDecimal("15.00")
            );
        } else {
            producto.setPorcentajeIva(BigDecimal.ZERO);
        }

        producto.setEstado(true);

        Producto productoGuardado = productoRepository.save(producto);

        return convertirAResponse(productoGuardado);
    }

    @Transactional
    public ProductoResponse actualizar(Integer id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"
                ));

        String codigoLimpio = request.getCodigoProducto().trim();
        String nombreLimpio = request.getNombreProducto().trim();

        productoRepository.findByCodigoProductoIgnoreCase(codigoLimpio)
                .ifPresent(productoExistente -> {
                    if (!productoExistente.getIdProducto().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Ya existe otro producto con ese código"
                        );
                    }
                });

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría no encontrada"
                ));

        if (Boolean.FALSE.equals(categoria.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede asignar una categoría inactiva"
            );
        }

        producto.setCategoria(categoria);
        producto.setCodigoProducto(codigoLimpio);
        producto.setNombreProducto(nombreLimpio);
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioCompra(valorONumeroCero(request.getPrecioCompra()));
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setStockMinimo(valorOEnteroCero(request.getStockMinimo()));

        /*
         * Importante:
         * No actualizamos stockActual aquí para no saltarnos el historial de inventario.
         * El stock se cambiará en el módulo Inventario usando la función ajustar_stock_producto().
         */

        Boolean aplicaIva = request.getAplicaIva() != null ? request.getAplicaIva() : true;
        producto.setAplicaIva(aplicaIva);

        if (Boolean.TRUE.equals(aplicaIva)) {
            producto.setPorcentajeIva(
                    request.getPorcentajeIva() != null
                            ? request.getPorcentajeIva()
                            : new BigDecimal("15.00")
            );
        } else {
            producto.setPorcentajeIva(BigDecimal.ZERO);
        }

        Producto productoActualizado = productoRepository.save(producto);

        return convertirAResponse(productoActualizado);
    }

    @Transactional
        public ProductoResponse desactivar(Integer idProducto) {
        Producto producto = buscarProductoPorId(idProducto);

        if (Boolean.FALSE.equals(producto.getEstado())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El producto ya se encuentra inactivo"
                );
        }

        producto.setEstado(false);

        Producto productoActualizado = productoRepository.save(producto);

        return convertirAResponse(productoActualizado);
        }

        @Transactional
        public ProductoResponse activar(Integer idProducto) {
        Producto producto = buscarProductoPorId(idProducto);

        if (Boolean.TRUE.equals(producto.getEstado())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El producto ya se encuentra activo"
                );
        }

        if (producto.getCategoria() == null || Boolean.FALSE.equals(producto.getCategoria().getEstado())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "No se puede activar el producto porque su categoría se encuentra inactiva"
                );
        }

        producto.setEstado(true);

        Producto productoActualizado = productoRepository.save(producto);

        return convertirAResponse(productoActualizado);
        }

        private Producto buscarProductoPorId(Integer idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"
                ));
        }
        

    private ProductoResponse convertirAResponse(Producto producto) {
        return new ProductoResponse(
                producto.getIdProducto(),
                producto.getCategoria().getIdCategoria(),
                producto.getCategoria().getNombreCategoria(),
                producto.getCodigoProducto(),
                producto.getNombreProducto(),
                producto.getDescripcion(),
                producto.getPrecioCompra(),
                producto.getPrecioVenta(),
                producto.getStockActual(),
                producto.getStockMinimo(),
                producto.getAplicaIva(),
                producto.getPorcentajeIva(),
                producto.getEstado(),
                producto.getFechaCreacion()
        );
    }

    private BigDecimal valorONumeroCero(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    private Integer valorOEnteroCero(Integer valor) {
        return valor != null ? valor : 0;
    }
}
