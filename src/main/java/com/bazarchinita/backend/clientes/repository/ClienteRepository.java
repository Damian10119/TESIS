package com.bazarchinita.backend.clientes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bazarchinita.backend.clientes.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByEstadoTrueOrderByNombresAsc();

    Optional<Cliente> findByIdentificacion(String identificacion);

    Optional<Cliente> findByEsConsumidorFinalTrue();

    boolean existsByIdentificacion(String identificacion);

    @Query("""
            SELECT c
            FROM Cliente c
            WHERE c.estado = true
              AND (
                    LOWER(c.nombres) LIKE LOWER(CONCAT('%', :texto, '%'))
                    OR LOWER(c.identificacion) LIKE LOWER(CONCAT('%', :texto, '%'))
                  )
            ORDER BY c.nombres ASC
            """)
    List<Cliente> buscarActivosPorNombreOIdentificacion(@Param("texto") String texto);
}
