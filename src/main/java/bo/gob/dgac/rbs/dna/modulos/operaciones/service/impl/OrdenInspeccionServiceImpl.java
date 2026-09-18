package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.modulos.catalogos.Estado;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.maper.OrdenInspeccionMapper;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.HistorialTransicion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.EstadoRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.HistorialTransicionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.OrdenInspeccionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.OrdenInspeccionService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class OrdenInspeccionServiceImpl implements OrdenInspeccionService {

    private final OrdenInspeccionRepository ordenRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final HistorialTransicionRepository historialRepository;
    private final OrdenInspeccionMapper ordenMapper;
    
    @Override
    @Transactional
    public OrdenInspeccionResponseDto crearOrden(OrdenInspeccionRequestDto requestDto) {
        // 1. Validar que no exista el código de orden
        if (ordenRepository.existsByCodigoOrden(requestDto.getCodigoOrden())) {
            throw new IllegalArgumentException("Ya existe una orden registrada con el código: " + requestDto.getCodigoOrden());
        }

        // 2. Obtener las entidades relacionadas
        Estado estadoInicial = estadoRepository.findById(requestDto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + requestDto.getEstadoId()));

        UsuarioRol director = usuarioRolRepository.findById(requestDto.getDirectorUsuarioRolId())
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioRol (Director) no encontrado con ID: " + requestDto.getDirectorUsuarioRolId()));

        // 3. Crear y guardar la Orden de Inspección
        OrdenInspeccion orden = OrdenInspeccion.builder()
                .codigoOrden(requestDto.getCodigoOrden())
                .titulo(requestDto.getTitulo())
                .estado(estadoInicial)
                .directorUsuarioRol(director)
                .build();

        OrdenInspeccion ordenGuardada = ordenRepository.save(orden);

        // 4. Registrar la creación en el historial de transiciones
        HistorialTransicion historialInicial = HistorialTransicion.builder()
                .ordenInspeccion(ordenGuardada)
                .estadoOrigen(null) // Es creación inicial
                .estadoDestino(estadoInicial)
                .accion("CREACION_ORDEN")
                .usuarioRol(director)
                .observaciones("Creación inicial de la orden de inspección por el Director")
                .build();

        historialRepository.save(historialInicial);

        // 5. Retornar el DTO mapeado
        return ordenMapper.toDto(ordenGuardada);
    }
    
    
    
    
    @Override
    @Transactional
    public OrdenInspeccion crear(OrdenInspeccion orden, Long estadoId, Long directorUsuarioRolId) {
        if (ordenRepository.existsByCodigoOrden(orden.getCodigoOrden())) {
            throw new IllegalArgumentException("El código de orden '" + orden.getCodigoOrden() + "' ya existe.");
        }

        Estado estado = estadoRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + estadoId));

        UsuarioRol director = usuarioRolRepository.findById(directorUsuarioRolId)
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioRol (Director) no encontrado con ID: " + directorUsuarioRolId));

        orden.setEstado(estado);
        orden.setDirectorUsuarioRol(director);

        return ordenRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenInspeccion actualizar(Long id, OrdenInspeccion ordenDetails, Long estadoId, Long directorUsuarioRolId) {
        OrdenInspeccion ordenExistente = obtenerPorId(id);

        if (!ordenExistente.getCodigoOrden().equals(ordenDetails.getCodigoOrden()) 
                && ordenRepository.existsByCodigoOrden(ordenDetails.getCodigoOrden())) {
            throw new IllegalArgumentException("El código de orden '" + ordenDetails.getCodigoOrden() + "' ya existe.");
        }

        if (estadoId != null) {
            Estado estado = estadoRepository.findById(estadoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + estadoId));
            ordenExistente.setEstado(estado);
        }

        if (directorUsuarioRolId != null) {
            UsuarioRol director = usuarioRolRepository.findById(directorUsuarioRolId)
                    .orElseThrow(() -> new ResourceNotFoundException("UsuarioRol (Director) no encontrado con ID: " + directorUsuarioRolId));
            ordenExistente.setDirectorUsuarioRol(director);
        }

        ordenExistente.setCodigoOrden(ordenDetails.getCodigoOrden());
        ordenExistente.setTitulo(ordenDetails.getTitulo());

        return ordenRepository.save(ordenExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenInspeccion obtenerPorId(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de Inspección no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenInspeccion obtenerPorCodigo(String codigoOrden) {
        return ordenRepository.findByCodigoOrden(codigoOrden)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de Inspección no encontrada con código: " + codigoOrden));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenInspeccion> obtenerTodos() {
        return ordenRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenInspeccion> obtenerTodosPaginado(Pageable pageable) {
        return ordenRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenInspeccion> obtenerPorEstado(Long estadoId, Pageable pageable) {
        return ordenRepository.findByEstadoId(estadoId, pageable);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        OrdenInspeccion orden = obtenerPorId(id);
        ordenRepository.delete(orden);
    }
}