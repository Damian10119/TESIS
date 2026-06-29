package com.bazarchinita.backend.categorias.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.categorias.dto.CategoriaRequest;
import com.bazarchinita.backend.categorias.dto.CategoriaResponse;
import com.bazarchinita.backend.categorias.entity.Categoria;
import com.bazarchinita.backend.categorias.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
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

    public CategoriaResponse crear(CategoriaRequest request) {
        String nombreLimpio = request.getNombreCategoria().trim();

        if (categoriaRepository.existsByNombreCategoriaIgnoreCase(nombreLimpio)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe una categoría con ese nombre"
            );
        }

        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(nombreLimpio);
        categoria.setDescripcion(request.getDescripcion());
        categoria.setEstado(true);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);

        return convertirAResponse(categoriaGuardada);
    }

    public CategoriaResponse actualizar(Integer id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría no encontrada"
                ));

        String nombreLimpio = request.getNombreCategoria().trim();

        categoriaRepository.findByNombreCategoriaIgnoreCase(nombreLimpio)
                .ifPresent(categoriaExistente -> {
                    if (!categoriaExistente.getIdCategoria().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Ya existe otra categoría con ese nombre"
                        );
                    }
                });

        categoria.setNombreCategoria(nombreLimpio);
        categoria.setDescripcion(request.getDescripcion());

        Categoria categoriaActualizada = categoriaRepository.save(categoria);

        return convertirAResponse(categoriaActualizada);
    }

    public CategoriaResponse desactivar(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría no encontrada"
                ));

        categoria.setEstado(false);

        Categoria categoriaDesactivada = categoriaRepository.save(categoria);

        return convertirAResponse(categoriaDesactivada);
    }

    private CategoriaResponse convertirAResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getIdCategoria(),
                categoria.getNombreCategoria(),
                categoria.getDescripcion(),
                categoria.getEstado()
        );
    }
}