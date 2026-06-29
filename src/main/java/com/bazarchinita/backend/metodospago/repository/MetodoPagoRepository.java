package com.bazarchinita.backend.metodospago.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarchinita.backend.metodospago.entity.MetodoPago;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {

    List<MetodoPago> findByEstadoTrueOrderByNombreMetodoAsc();

    Optional<MetodoPago> findByNombreMetodoIgnoreCase(String nombreMetodo);
}
