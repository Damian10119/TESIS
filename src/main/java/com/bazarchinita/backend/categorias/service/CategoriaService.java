package com.bazarchinita.backend.categorias.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import com.bazarchinita.backend.productos.repository.ProductoRepository;
import com.bazarchinita.backend.categorias.dto.CategoriaRequest;
import com.bazarchinita.backend.categorias.dto.CategoriaResponse;
import com.bazarchinita.backend.categorias.entity.Categoria;
import com.bazarchinita.backend.categorias.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaService(
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    public List<CategoriaResponse> listarCategoriasActivas() {
        return categoriaRepository.findByEstadoTrueOrderByNombreCategoriaAsc()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public CategoriaResponse buscarPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría no encontrada"
                ));

        return convertirAResponse(categoria);
    }

    
    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        String nombreCategoria = normalizarNombreParaGuardar(request.getNombreCategoria());

        validarNombreDisponibleParaCrear(nombreCategoria);

        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(nombreCategoria);
        categoria.setDescripcion(limpiarTexto(request.getDescripcion()));
        categoria.setEstado(true);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);

        return convertirAResponse(categoriaGuardada);
    }

    @Transactional
        public CategoriaResponse actualizar(Integer idCategoria, CategoriaRequest request) {
            Categoria categoria = buscarCategoriaPorId(idCategoria);

            String nombreCategoria = normalizarNombreParaGuardar(request.getNombreCategoria());

            validarNombreDisponibleParaActualizar(nombreCategoria, idCategoria);

            categoria.setNombreCategoria(nombreCategoria);
            categoria.setDescripcion(limpiarTexto(request.getDescripcion()));

            Categoria categoriaActualizada = categoriaRepository.save(categoria);

            return convertirAResponse(categoriaActualizada);
        }

    @Transactional
    public CategoriaResponse desactivar(Integer idCategoria) {
        Categoria categoria = buscarCategoriaPorId(idCategoria);

        if (Boolean.FALSE.equals(categoria.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría ya se encuentra inactiva"
            );
        }

        boolean tieneProductosActivos = productoRepository.existeProductoActivoPorCategoria(idCategoria);

        if (tieneProductosActivos) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede desactivar la categoría porque tiene productos activos asociados"
            );
        }

        categoria.setEstado(false);

        Categoria categoriaActualizada = categoriaRepository.save(categoria);

        return convertirAResponse(categoriaActualizada);
    }

    @Transactional
    public CategoriaResponse activar(Integer idCategoria) {
        Categoria categoria = buscarCategoriaPorId(idCategoria);

        if (Boolean.TRUE.equals(categoria.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría ya se encuentra activa"
            );
        }

        validarNombreDisponibleParaActivar(categoria.getNombreCategoria(), idCategoria);

        categoria.setEstado(true);

        Categoria categoriaActualizada = categoriaRepository.save(categoria);

        return convertirAResponse(categoriaActualizada);
    }

    private CategoriaResponse convertirAResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getIdCategoria(),
                categoria.getNombreCategoria(),
                categoria.getDescripcion(),
                categoria.getEstado()
        );
    }

    private Categoria buscarCategoriaPorId(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría no encontrada"
                ));
    }

    private String normalizarNombreParaGuardar(String nombreCategoria) {
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre de la categoría es obligatorio"
            );
        }

        return nombreCategoria.trim().replaceAll("\\s+", " ");
    }

    private String limpiarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }

        return texto.trim().replaceAll("\\s+", " ");
    }

    private void validarNombreDisponibleParaCrear(String nombreCategoria) {
        categoriaRepository.buscarPorNombreNormalizado(nombreCategoria)
                .ifPresent(categoriaExistente -> {
                    if (Boolean.TRUE.equals(categoriaExistente.getEstado())) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Ya existe una categoría activa con ese nombre"
                        );
                    }

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Ya existe una categoría inactiva con ese nombre. Puede reactivarla desde el listado de categorías"
                    );
                });
    }

    private void validarNombreDisponibleParaActualizar(
            String nombreCategoria,
            Integer idCategoriaActual
    ) {
        categoriaRepository.buscarPorNombreNormalizado(nombreCategoria)
                .ifPresent(categoriaExistente -> {
                    if (!categoriaExistente.getIdCategoria().equals(idCategoriaActual)) {
                        if (Boolean.TRUE.equals(categoriaExistente.getEstado())) {
                            throw new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "Ya existe otra categoría activa con ese nombre"
                            );
                        }

                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Ya existe otra categoría inactiva con ese nombre. Puede reactivarla en lugar de duplicarla"
                        );
                    }
                });
    }

    private void validarNombreDisponibleParaActivar(
            String nombreCategoria,
            Integer idCategoriaActual
    ) {
        categoriaRepository.buscarPorNombreNormalizado(nombreCategoria)
                .ifPresent(categoriaExistente -> {
                    if (!categoriaExistente.getIdCategoria().equals(idCategoriaActual)) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "No se puede reactivar la categoría porque ya existe otra categoría con ese nombre"
                        );
                    }
                });
    }
}