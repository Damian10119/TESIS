package com.bazarchinita.backend.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.http.HttpMethod;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityExceptionHandler securityExceptionHandler;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            SecurityExceptionHandler securityExceptionHandler,
            UserDetailsService userDetailsService
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.securityExceptionHandler = securityExceptionHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityExceptionHandler)
                        .accessDeniedHandler(securityExceptionHandler)
                )
                .authorizeHttpRequests(auth -> auth

                        // Rutas públicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/system/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // Usuarios y roles: solo administrador
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/roles/**").hasRole("ADMINISTRADOR")

                        // Configuración del negocio
                        .requestMatchers(HttpMethod.GET, "/api/configuracion-negocio/**")
                            .hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                        .requestMatchers("/api/configuracion-negocio/**")
                            .hasRole("ADMINISTRADOR")

                        // Configuración de pagos
                        .requestMatchers(HttpMethod.GET, "/api/configuracion-pagos/**")
                            .hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                        .requestMatchers("/api/configuracion-pagos/**")
                            .hasRole("ADMINISTRADOR")

                        // Métodos de pago: consulta para ambos roles
                        .requestMatchers(HttpMethod.GET, "/api/metodos-pago/**")
                            .hasAnyRole("ADMINISTRADOR", "VENDEDOR")

                        // Categorías: consulta ambos, gestión solo administrador
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**")
                            .hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                        .requestMatchers("/api/categorias/**")
                            .hasRole("ADMINISTRADOR")

                        // Productos: consulta ambos, gestión solo administrador
                        .requestMatchers(HttpMethod.GET, "/api/productos/**")
                            .hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                        .requestMatchers("/api/productos/**")
                            .hasRole("ADMINISTRADOR")

                        // Clientes: administrador y vendedor pueden gestionar
                        .requestMatchers("/api/clientes/**")
                            .hasAnyRole("ADMINISTRADOR", "VENDEDOR")

                        .requestMatchers("/api/ventas/**")
                            .hasAnyRole("ADMINISTRADOR", "VENDEDOR")

                        .requestMatchers("/api/reportes/**")
                            .hasAnyRole("ADMINISTRADOR", "VENDEDOR")

                        // Cualquier otra ruta futura requiere login
                        .anyRequest().authenticated()
                        )
                
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }
        };
    }
}
