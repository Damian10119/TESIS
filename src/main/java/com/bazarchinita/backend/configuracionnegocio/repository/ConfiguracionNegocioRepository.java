package com.bazarchinita.backend.configuracionnegocio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarchinita.backend.configuracionnegocio.entity.ConfiguracionNegocio;

public interface ConfiguracionNegocioRepository extends JpaRepository<ConfiguracionNegocio, Integer> {

    Optional<ConfiguracionNegocio> findFirstByEstadoTrueOrderByIdConfiguracionAsc();

    List<ConfiguracionNegocio> findByEstadoTrueOrderByIdConfiguracionAsc();
}