package com.bazarchinita.backend.clientes.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.clientes.dto.ClienteRequest;
import com.bazarchinita.backend.clientes.dto.ClienteResponse;
import com.bazarchinita.backend.clientes.entity.Cliente;
import com.bazarchinita.backend.clientes.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarActivos() {
        return clienteRepository.findByEstadoTrueOrderByNombresAsc()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado"
                ));

        return convertirAResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorIdentificacion(String identificacion) {
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado"
                ));

        return convertirAResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtenerConsumidorFinal() {
        Cliente cliente = clienteRepository.findByEsConsumidorFinalTrue()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consumidor Final no configurado en la base de datos"
                ));

        return convertirAResponse(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> buscar(String texto) {
        return clienteRepository.buscarActivosPorNombreOIdentificacion(texto.trim())
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        String tipo = normalizarTipoIdentificacion(request.getTipoIdentificacion());
        String identificacion = request.getIdentificacion().trim();
        String nombres = request.getNombres().trim();

        validarTipoIdentificacion(tipo);
        validarIdentificacionSegunTipo(tipo, identificacion);

        if ("CONSUMIDOR_FINAL".equals(tipo)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El Consumidor Final ya se encuentra configurado en la base de datos"
            );
        }

        if (clienteRepository.existsByIdentificacion(identificacion)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe un cliente con esa identificación"
            );
        }

        Cliente cliente = new Cliente();
        cliente.setTipoIdentificacion(tipo);
        cliente.setIdentificacion(identificacion);
        cliente.setNombres(nombres);
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setCorreo(request.getCorreo());
        cliente.setEsConsumidorFinal(false);
        cliente.setEstado(true);

        Cliente clienteGuardado = clienteRepository.saveAndFlush(cliente);

        return convertirAResponse(clienteGuardado);
    }

    @Transactional
    public ClienteResponse actualizar(Integer id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado"
                ));

        if (Boolean.TRUE.equals(cliente.getEsConsumidorFinal())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede modificar el Consumidor Final desde este módulo"
            );
        }

        String tipo = normalizarTipoIdentificacion(request.getTipoIdentificacion());
        String identificacion = request.getIdentificacion().trim();
        String nombres = request.getNombres().trim();

        validarTipoIdentificacion(tipo);
        validarIdentificacionSegunTipo(tipo, identificacion);

        if ("CONSUMIDOR_FINAL".equals(tipo)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede convertir un cliente normal en Consumidor Final"
            );
        }

        clienteRepository.findByIdentificacion(identificacion)
                .ifPresent(clienteExistente -> {
                    if (!clienteExistente.getIdCliente().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Ya existe otro cliente con esa identificación"
                        );
                    }
                });

        cliente.setTipoIdentificacion(tipo);
        cliente.setIdentificacion(identificacion);
        cliente.setNombres(nombres);
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setCorreo(request.getCorreo());

        Cliente clienteActualizado = clienteRepository.saveAndFlush(cliente);

        return convertirAResponse(clienteActualizado);
    }

    @Transactional
    public ClienteResponse desactivar(Integer idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);

        if (Boolean.TRUE.equals(cliente.getEsConsumidorFinal())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede desactivar el consumidor final"
            );
        }

        if (Boolean.FALSE.equals(cliente.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El cliente ya se encuentra inactivo"
            );
        }

        cliente.setEstado(false);

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return convertirAResponse(clienteActualizado);
    }

    @Transactional
    public ClienteResponse activar(Integer idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);

        if (Boolean.TRUE.equals(cliente.getEsConsumidorFinal())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El consumidor final debe permanecer activo"
            );
        }

        if (Boolean.TRUE.equals(cliente.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El cliente ya se encuentra activo"
            );
        }

        cliente.setEstado(true);

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return convertirAResponse(clienteActualizado);
    }


    private Cliente buscarClientePorId(Integer idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado"
                ));
    }

    private ClienteResponse convertirAResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getIdCliente(),
                cliente.getTipoIdentificacion(),
                cliente.getIdentificacion(),
                cliente.getNombres(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getCorreo(),
                cliente.getEsConsumidorFinal(),
                cliente.getEstado(),
                cliente.getFechaCreacion()
        );
    }

    private String normalizarTipoIdentificacion(String tipoIdentificacion) {
        return tipoIdentificacion.trim().toUpperCase();
    }

    private void validarTipoIdentificacion(String tipo) {
        if (!tipo.equals("CEDULA")
                && !tipo.equals("RUC")
                && !tipo.equals("PASAPORTE")
                && !tipo.equals("CONSUMIDOR_FINAL")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de identificación no válido. Use CEDULA, RUC o PASAPORTE"
            );
        }
    }

    private void validarIdentificacionSegunTipo(String tipo, String identificacion) {
        if ("CEDULA".equals(tipo) && !identificacion.matches("\\d{10}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cédula debe tener 10 dígitos numéricos"
            );
        }

        if ("RUC".equals(tipo) && !identificacion.matches("\\d{13}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El RUC debe tener 13 dígitos numéricos"
            );
        }

        if ("PASAPORTE".equals(tipo) && identificacion.length() < 5) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El pasaporte debe tener al menos 5 caracteres"
            );
        }
    }
}