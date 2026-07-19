package com.bazarchinita.backend.categorias.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bazarchinita.backend.categorias.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findByEstadoTrueOrderByNombreCategoriaAsc();

    Optional<Categoria> findByNombreCategoriaIgnoreCase(String nombreCategoria);

    boolean existsByNombreCategoriaIgnoreCase(String nombreCategoria);

    @Query("""
            SELECT c
            FROM Categoria c
            WHERE LOWER(TRIM(c.nombreCategoria)) = LOWER(TRIM(:nombreCategoria))
            """)
    Optional<Categoria> buscarPorNombreNormalizado(
            @Param("nombreCategoria") String nombreCategoria
    );
}