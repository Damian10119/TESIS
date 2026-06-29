package com.bazarchinita.backend.configuracionnegocio.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.configuracionnegocio.dto.ConfiguracionNegocioRequest;
import com.bazarchinita.backend.configuracionnegocio.dto.ConfiguracionNegocioResponse;
import com.bazarchinita.backend.configuracionnegocio.entity.ConfiguracionNegocio;
import com.bazarchinita.backend.configuracionnegocio.repository.ConfiguracionNegocioRepository;

@Service
public class ConfiguracionNegocioService {

    private final ConfiguracionNegocioRepository configuracionNegocioRepository;

    public ConfiguracionNegocioService(ConfiguracionNegocioRepository configuracionNegocioRepository) {
        this.configuracionNegocioRepository = configuracionNegocioRepository;
    }

    @Transactional(readOnly = true)
    public ConfiguracionNegocioResponse obtenerActual() {
        ConfiguracionNegocio configuracion = configuracionNegocioRepository
                .findFirstByEstadoTrueOrderByIdConfiguracionAsc()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una configuración activa del negocio"
                ));

        return convertirAResponse(configuracion);
    }

    @Transactional(readOnly = true)
    public List<ConfiguracionNegocioResponse> listarActivas() {
        return configuracionNegocioRepository.findByEstadoTrueOrderByIdConfiguracionAsc()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConfiguracionNegocioResponse> listarTodas() {
        return configuracionNegocioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConfiguracionNegocioResponse buscarPorId(Integer id) {
        ConfiguracionNegocio configuracion = configuracionNegocioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Configuración del negocio no encontrada"
                ));

        return convertirAResponse(configuracion);
    }

    @Transactional
    public ConfiguracionNegocioResponse actualizar(Integer id, ConfiguracionNegocioRequest request) {
        ConfiguracionNegocio configuracion = configuracionNegocioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Configuración del negocio no encontrada"
                ));

        String nombreNegocio = request.getNombreNegocio().trim();
        String ruc = limpiarTextoONull(request.getRuc());
        String direccion = limpiarTextoONull(request.getDireccion());
        String telefono = limpiarTextoONull(request.getTelefono());
        String correo = limpiarTextoONull(request.getCorreo());
        String ambiente = normalizarAmbiente(request.getAmbienteFacturacion());

        validarRucSiExiste(ruc);
        validarAmbienteFacturacion(ambiente);

        configuracion.setNombreNegocio(nombreNegocio);
        configuracion.setRuc(ruc);
        configuracion.setDireccion(direccion);
        configuracion.setTelefono(telefono);
        configuracion.setCorreo(correo);
        configuracion.setAmbienteFacturacion(ambiente);
        configuracion.setObligadoContabilidad(
                request.getObligadoContabilidad() != null
                        ? request.getObligadoContabilidad()
                        : false
        );

        /*
         * La configuración principal del negocio debe mantenerse activa.
         * Por eso no permitimos desactivarla desde este módulo.
         */
        configuracion.setEstado(true);

        ConfiguracionNegocio configuracionActualizada =
                configuracionNegocioRepository.saveAndFlush(configuracion);

        return convertirAResponse(configuracionActualizada);
    }

    private ConfiguracionNegocioResponse convertirAResponse(ConfiguracionNegocio configuracion) {
        return new ConfiguracionNegocioResponse(
                configuracion.getIdConfiguracion(),
                configuracion.getNombreNegocio(),
                configuracion.getRuc(),
                configuracion.getDireccion(),
                configuracion.getTelefono(),
                configuracion.getCorreo(),
                configuracion.getAmbienteFacturacion(),
                configuracion.getObligadoContabilidad(),
                configuracion.getEstado()
        );
    }

    private String limpiarTextoONull(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }

        return texto.trim();
    }

    private String normalizarAmbiente(String ambiente) {
        if (ambiente == null || ambiente.trim().isEmpty()) {
            return "PRUEBAS";
        }

        return ambiente.trim().toUpperCase();
    }

    private void validarAmbienteFacturacion(String ambiente) {
        if (!ambiente.equals("PRUEBAS") && !ambiente.equals("PRODUCCION")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ambiente de facturación debe ser PRUEBAS o PRODUCCION"
            );
        }
    }

    private void validarRucSiExiste(String ruc) {
        if (ruc != null && !ruc.matches("\\d{13}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El RUC debe tener 13 dígitos numéricos"
            );
        }
    }
}
