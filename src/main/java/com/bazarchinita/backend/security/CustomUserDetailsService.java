package com.bazarchinita.backend.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bazarchinita.backend.usuarios.entity.Usuario;
import com.bazarchinita.backend.usuarios.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (Boolean.FALSE.equals(usuario.getEstado())) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        String rolSecurity = convertirRolASecurity(usuario.getRol().getNombreRol());

        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getContrasena())
                .authorities(rolSecurity)
                .disabled(Boolean.FALSE.equals(usuario.getEstado()))
                .build();
    }

    private String convertirRolASecurity(String nombreRol) {
        String rol = nombreRol
                .trim()
                .toUpperCase()
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace(" ", "_");

        return "ROLE_" + rol;
    }
}