package com.bazarchinita.backend.roles.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bazarchinita.backend.roles.dto.RolResponse;
import com.bazarchinita.backend.roles.entity.Rol;
import com.bazarchinita.backend.roles.repository.RolRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<RolResponse> listarActivos() {
        return rolRepository.findByEstadoTrueOrderByNombreRolAsc()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    private RolResponse convertirAResponse(Rol rol) {
        return new RolResponse(
                rol.getIdRol(),
                rol.getNombreRol(),
                rol.getDescripcion(),
                rol.getEstado()
        );
    }
}
