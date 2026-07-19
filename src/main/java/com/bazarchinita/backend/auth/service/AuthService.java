package com.bazarchinita.backend.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bazarchinita.backend.auth.dto.LoginRequest;
import com.bazarchinita.backend.auth.dto.LoginResponse;
import com.bazarchinita.backend.security.JwtService;
import com.bazarchinita.backend.usuarios.entity.Usuario;
import com.bazarchinita.backend.usuarios.repository.UsuarioRepository;
import com.bazarchinita.backend.auth.dto.CambiarContrasenaRequest;
import com.bazarchinita.backend.auth.dto.CambiarContrasenaResponse;
import com.bazarchinita.backend.auth.dto.PerfilUsuarioResponse;

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

    @Transactional(readOnly = true)
    public PerfilUsuarioResponse obtenerPerfilActual() {
        Usuario usuario = obtenerUsuarioActual();

        return new PerfilUsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getNombres(),
                usuario.getCorreo(),
                usuario.getRol().getIdRol(),
                usuario.getRol().getNombreRol(),
                usuario.getEstado()
        );
    }

    @Transactional
    public CambiarContrasenaResponse cambiarContrasena(CambiarContrasenaRequest request) {
        Usuario usuario = obtenerUsuarioActual();

        String contrasenaActual = requerido(request.getContrasenaActual(), "La contraseña actual es obligatoria");
        String nuevaContrasena = requerido(request.getNuevaContrasena(), "La nueva contraseña es obligatoria");
        String confirmarContrasena = requerido(request.getConfirmarContrasena(), "La confirmación de contraseña es obligatoria");

        // 1. Primero se valida que la contraseña actual sea correcta
        if (!contrasenaCoincide(contrasenaActual, usuario.getContrasena())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña actual es incorrecta"
            );
        }

        // 2. Luego se valida la nueva contraseña
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La nueva contraseña y la confirmación no coinciden"
            );
        }

        if (nuevaContrasena.length() < 8) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La nueva contraseña debe tener al menos 8 caracteres"
            );
        }

        if (contrasenaActual.equals(nuevaContrasena)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La nueva contraseña no puede ser igual a la contraseña actual"
            );
        }

        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);

        return new CambiarContrasenaResponse(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                "Contraseña actualizada correctamente"
        );
    }

    private Usuario obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "No se pudo identificar el usuario autenticado"
            );
        }

        String nombreUsuario = authentication.getName();

        return usuarioRepository.findByNombreUsuarioIgnoreCase(nombreUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "No se pudo identificar el usuario autenticado"
                ));
    }

    private boolean contrasenaCoincide(String contrasenaIngresada, String contrasenaGuardada) {
        if (contrasenaGuardada == null) {
            return false;
        }

        if (esPasswordBCrypt(contrasenaGuardada)) {
            return passwordEncoder.matches(contrasenaIngresada, contrasenaGuardada);
        }

        return contrasenaIngresada.equals(contrasenaGuardada);
    }

    private boolean esPasswordBCrypt(String password) {
        return password != null
                && (password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$"));
    }

    private String requerido(String valor, String mensajeError) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensajeError);
        }
        return valor.trim();
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        String nombreUsuario = requerido(request.getNombreUsuario(), "El nombre de usuario es obligatorio").toLowerCase();
        String contrasenaIngresada = requerido(request.getContrasena(), "La contraseña es obligatoria");

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
        boolean passwordCorrecta;

        if (esPasswordBCrypt(contrasenaGuardada)) {
            passwordCorrecta = passwordEncoder.matches(contrasenaIngresada, contrasenaGuardada);
        } else {
            /*
             * Migración temporal:
             * Si el usuario fue creado antes de activar Spring Security,
             * la contraseña puede estar en texto plano.
             * Si coincide, se reemplaza por BCrypt automáticamente.
             *
             * TODO: eliminar esta rama una vez confirmada la migración
             * completa de todos los usuarios a BCrypt.
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
}