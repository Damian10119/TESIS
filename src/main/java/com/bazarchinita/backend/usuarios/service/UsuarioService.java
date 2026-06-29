package com.bazarchinita.backend.usuarios.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.roles.entity.Rol;
import com.bazarchinita.backend.roles.repository.RolRepository;
import com.bazarchinita.backend.usuarios.dto.UsuarioRequest;
import com.bazarchinita.backend.usuarios.dto.UsuarioResponse;
import com.bazarchinita.backend.usuarios.dto.UsuarioUpdateRequest;
import com.bazarchinita.backend.usuarios.entity.Usuario;
import com.bazarchinita.backend.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

            public UsuarioService(
                UsuarioRepository usuarioRepository,
                RolRepository rolRepository,
                PasswordEncoder passwordEncoder
        ) {
            this.usuarioRepository = usuarioRepository;
            this.rolRepository = rolRepository;
            this.passwordEncoder = passwordEncoder;
        }
    

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarActivos() {
        return usuarioRepository.findByEstadoTrueOrderByNombresAsc()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return convertirAResponse(usuario);
    }

    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        String nombreUsuario = request.getNombreUsuario().trim().toLowerCase();

        if (usuarioRepository.existsByNombreUsuarioIgnoreCase(nombreUsuario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese nombre");
        }

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

        if (Boolean.FALSE.equals(rol.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede asignar un rol inactivo");
        }

        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena())); // Luego la encriptamos con BCrypt al integrar Security.
        usuario.setNombres(request.getNombres().trim());
        usuario.setCorreo(limpiarTextoONull(request.getCorreo()));
        usuario.setEstado(true);

        Usuario usuarioGuardado = usuarioRepository.saveAndFlush(usuario);
        return convertirAResponse(usuarioGuardado);
    }

    @Transactional
    public UsuarioResponse actualizar(Integer id, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

        if (Boolean.FALSE.equals(rol.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede asignar un rol inactivo");
        }

        usuario.setRol(rol);
        usuario.setNombres(request.getNombres().trim());
        usuario.setCorreo(limpiarTextoONull(request.getCorreo()));

        Usuario usuarioActualizado = usuarioRepository.saveAndFlush(usuario);
        return convertirAResponse(usuarioActualizado);
    }

    @Transactional
    public UsuarioResponse desactivar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        usuario.setEstado(false);

        Usuario usuarioDesactivado = usuarioRepository.saveAndFlush(usuario);
        return convertirAResponse(usuarioDesactivado);
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getRol().getIdRol(),
                usuario.getRol().getNombreRol(),
                usuario.getNombreUsuario(),
                usuario.getNombres(),
                usuario.getCorreo(),
                usuario.getEstado(),
                usuario.getFechaCreacion()
        );
    }

    private String limpiarTextoONull(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        return texto.trim();
    }
}
