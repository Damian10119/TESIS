package com.bazarchinita.backend.roles.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.roles.dto.RolResponse;
import com.bazarchinita.backend.roles.service.RolService;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<RolResponse> listarActivos() {
        return rolService.listarActivos();
    }
}
