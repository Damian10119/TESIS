package com.bazarchinita.backend.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.auth.dto.LoginRequest;
import com.bazarchinita.backend.auth.dto.LoginResponse;
import com.bazarchinita.backend.security.JwtService;
import com.bazarchinita.backend.usuarios.entity.Usuario;
import com.bazarchinita.backend.usuarios.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        String nombreUsuario = request.getNombreUsuario().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(nombreUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario o contraseña incorrectos"
                ));

        if (Boolean.FALSE.equals(usuario.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "El usuario se encuentra inactivo"
            );
        }

        String contrasenaGuardada = usuario.getContrasena();
        String contrasenaIngresada = request.getContrasena();

        boolean passwordCorrecta;

        if (esPasswordBCrypt(contrasenaGuardada)) {
            passwordCorrecta = passwordEncoder.matches(contrasenaIngresada, contrasenaGuardada);
        } else {
            /*
             * Migración temporal:
             * Si el usuario fue creado antes de activar Spring Security,
             * la contraseña puede estar en texto plano.
             * Si coincide, se reemplaza por BCrypt automáticamente.
             */
            passwordCorrecta = contrasenaIngresada.equals(contrasenaGuardada);

            if (passwordCorrecta) {
                usuario.setContrasena(passwordEncoder.encode(contrasenaIngresada));
                usuarioRepository.saveAndFlush(usuario);
            }
        }

        if (!passwordCorrecta) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario o contraseña incorrectos"
            );
        }

        String token = jwtService.generarToken(usuario);

        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getJwtExpirationMs(),
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getNombres(),
                usuario.getRol().getIdRol(),
                usuario.getRol().getNombreRol()
        );
    }

    private boolean esPasswordBCrypt(String password) {
        return password != null
                && (password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$"));
    }
}
