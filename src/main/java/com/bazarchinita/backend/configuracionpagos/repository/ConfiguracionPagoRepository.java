package com.bazarchinita.backend.configuracionpagos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarchinita.backend.configuracionpagos.entity.ConfiguracionPago;

public interface ConfiguracionPagoRepository extends JpaRepository<ConfiguracionPago, Integer> {

    List<ConfiguracionPago> findByEstadoTrueOrderByIdConfiguracionPagoAsc();

    Optional<ConfiguracionPago> findFirstByMetodoPagoNombreMetodoIgnoreCase(String nombreMetodo);
}