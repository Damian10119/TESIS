package com.bazarchinita.backend.configuracionpagos.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.configuracionpagos.dto.ConfiguracionPagoRequest;
import com.bazarchinita.backend.configuracionpagos.dto.ConfiguracionPagoResponse;
import com.bazarchinita.backend.configuracionpagos.entity.ConfiguracionPago;
import com.bazarchinita.backend.configuracionpagos.repository.ConfiguracionPagoRepository;

@Service
public class ConfiguracionPagoService {

    private final ConfiguracionPagoRepository configuracionPagoRepository;

    public ConfiguracionPagoService(ConfiguracionPagoRepository configuracionPagoRepository) {
        this.configuracionPagoRepository = configuracionPagoRepository;
    }

    @Transactional(readOnly = true)
    public List<ConfiguracionPagoResponse> listarActivas() {
        return configuracionPagoRepository.findByEstadoTrueOrderByIdConfiguracionPagoAsc()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConfiguracionPagoResponse> listarTodas() {
        return configuracionPagoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConfiguracionPagoResponse buscarPorId(Integer id) {
        ConfiguracionPago configuracionPago = configuracionPagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Configuración de pago no encontrada"
                ));

        return convertirAResponse(configuracionPago);
    }

    @Transactional(readOnly = true)
    public ConfiguracionPagoResponse buscarPorMetodo(String nombreMetodo) {
        ConfiguracionPago configuracionPago = configuracionPagoRepository
                .findFirstByMetodoPagoNombreMetodoIgnoreCase(nombreMetodo.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe configuración para ese método de pago"
                ));

        return convertirAResponse(configuracionPago);
    }

    @Transactional
    public ConfiguracionPagoResponse actualizar(Integer id, ConfiguracionPagoRequest request) {
        ConfiguracionPago configuracionPago = configuracionPagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Configuración de pago no encontrada"
                ));

        configuracionPago.setNombreCuenta(request.getNombreCuenta());
        configuracionPago.setEntidad(request.getEntidad());
        configuracionPago.setNumeroCuenta(request.getNumeroCuenta());
        configuracionPago.setUrlQr(request.getUrlQr());
        configuracionPago.setInstrucciones(request.getInstrucciones());

        if (request.getEstado() != null) {
            configuracionPago.setEstado(request.getEstado());
        }

        ConfiguracionPago configuracionActualizada = configuracionPagoRepository.saveAndFlush(configuracionPago);

        return convertirAResponse(configuracionActualizada);
    }

    private ConfiguracionPagoResponse convertirAResponse(ConfiguracionPago configuracionPago) {
        return new ConfiguracionPagoResponse(
                configuracionPago.getIdConfiguracionPago(),
                configuracionPago.getMetodoPago().getIdMetodoPago(),
                configuracionPago.getMetodoPago().getNombreMetodo(),
                configuracionPago.getMetodoPago().getRequiereReferencia(),
                configuracionPago.getNombreCuenta(),
                configuracionPago.getEntidad(),
                configuracionPago.getNumeroCuenta(),
                configuracionPago.getUrlQr(),
                configuracionPago.getInstrucciones(),
                configuracionPago.getEstado()
        );
    }
}