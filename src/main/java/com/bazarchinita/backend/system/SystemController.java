package com.bazarchinita.backend.system;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    private final JdbcTemplate jdbcTemplate;

    public SystemController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db")
    public Map<String, Object> verificarBaseDatos() {
        String nombreBase = jdbcTemplate.queryForObject(
                "SELECT current_database()",
                String.class
        );

        Integer totalRoles = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM roles",
                Integer.class
        );

        Integer totalMetodosPago = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM metodos_pago",
                Integer.class
        );

        return Map.of(
                "estado", "OK",
                "baseDatos", nombreBase,
                "rolesRegistrados", totalRoles,
                "metodosPagoRegistrados", totalMetodosPago
        );
    }
}
