package com.bazarchinita.backend.categorias.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarchinita.backend.categorias.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findByEstadoTrueOrderByNombreCategoriaAsc();

    Optional<Categoria> findByNombreCategoriaIgnoreCase(String nombreCategoria);

    boolean existsByNombreCategoriaIgnoreCase(String nombreCategoria);
}