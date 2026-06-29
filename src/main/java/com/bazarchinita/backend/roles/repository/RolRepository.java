package com.bazarchinita.backend.roles.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarchinita.backend.roles.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    List<Rol> findByEstadoTrueOrderByNombreRolAsc();
}
