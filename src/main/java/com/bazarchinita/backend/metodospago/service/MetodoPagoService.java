package com.bazarchinita.backend.metodospago.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.metodospago.dto.MetodoPagoResponse;
import com.bazarchinita.backend.metodospago.entity.MetodoPago;
import com.bazarchinita.backend.metodospago.repository.MetodoPagoRepository;

@Service
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    public MetodoPagoService(MetodoPagoRepository metodoPagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
    }

    @Transactional(readOnly = true)
    public List<MetodoPagoResponse> listarActivos() {
        return metodoPagoRepository.findByEstadoTrueOrderByNombreMetodoAsc()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MetodoPagoResponse> listarTodos() {
        return metodoPagoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MetodoPagoResponse buscarPorId(Integer id) {
        MetodoPago metodoPago = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Método de pago no encontrado"
                ));

        return convertirAResponse(metodoPago);
    }

    @Transactional(readOnly = true)
    public MetodoPagoResponse buscarPorNombre(String nombre) {
        MetodoPago metodoPago = metodoPagoRepository.findByNombreMetodoIgnoreCase(nombre.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Método de pago no encontrado"
                ));

        return convertirAResponse(metodoPago);
    }

    private MetodoPagoResponse convertirAResponse(MetodoPago metodoPago) {
        return new MetodoPagoResponse(
                metodoPago.getIdMetodoPago(),
                metodoPago.getNombreMetodo(),
                metodoPago.getDescripcion(),
                metodoPago.getRequiereReferencia(),
                metodoPago.getEstado()
        );
    }
}