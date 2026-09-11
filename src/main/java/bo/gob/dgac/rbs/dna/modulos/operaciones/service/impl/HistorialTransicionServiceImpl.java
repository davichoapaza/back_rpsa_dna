package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.modulos.catalogos.Estado;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.HistorialTransicionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.HistorialTransicionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.maper.HistorialTransicionMapper;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.HistorialTransicion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.EstadoRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.HistorialTransicionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.InspectorVigilanciaRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.JefaturaAsignacionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.OrdenInspeccionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.HistorialTransicionService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistorialTransicionServiceImpl implements HistorialTransicionService {

    private final HistorialTransicionRepository historialRepository;
    private final OrdenInspeccionRepository ordenRepository;
    private final JefaturaAsignacionRepository jefaturaRepository;
    private final InspectorVigilanciaRepository inspectorRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final HistorialTransicionMapper mapper;

    @Override
    @Transactional
    public HistorialTransicionResponseDto registrarTransicion(HistorialTransicionRequestDto dto) {
        OrdenInspeccion orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + dto.getOrdenId()));

        JefaturaAsignacion jefatura = null;
        if (dto.getJefaturaId() != null) {
            jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Jefatura no encontrada con ID: " + dto.getJefaturaId()));
        }

        InspectorVigilancia tarea = null;
        if (dto.getTareaId() != null) {
            tarea = inspectorRepository.findById(dto.getTareaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + dto.getTareaId()));
        }

        Estado estadoOrigen = null;
        if (dto.getEstadoOrigenId() != null) {
            estadoOrigen = estadoRepository.findById(dto.getEstadoOrigenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Estado Origen no encontrado con ID: " + dto.getEstadoOrigenId()));
        }

        Estado estadoDestino = estadoRepository.findById(dto.getEstadoDestinoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado Destino no encontrado con ID: " + dto.getEstadoDestinoId()));

        UsuarioRol usuarioRol = usuarioRolRepository.findById(dto.getUsuarioRolId())
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioRol no encontrado con ID: " + dto.getUsuarioRolId()));

        HistorialTransicion historial = HistorialTransicion.builder()
                .ordenInspeccion(orden)
                .jefaturaAsignacion(jefatura)
                .tarea(tarea)
                .estadoOrigen(estadoOrigen)
                .estadoDestino(estadoDestino)
                .accion(dto.getAccion())
                .usuarioRol(usuarioRol)
                .observaciones(dto.getObservaciones())
                .build();

        return mapper.toDto(historialRepository.save(historial));
    }

    @Override
    @Transactional(readOnly = true)
    public HistorialTransicionResponseDto obtenerPorId(Long id) {
        return historialRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de historial no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialTransicionResponseDto> obtenerHistorialPorOrden(Long ordenId) {
        return mapper.toDtoList(historialRepository.findByOrdenInspeccionIdOrderByFechaTransicionAsc(ordenId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistorialTransicionResponseDto> obtenerHistorialPorOrdenPaginado(Long ordenId, Pageable pageable) {
        return historialRepository.findByOrdenInspeccionId(ordenId, pageable)
                .map(mapper::toDto);
    }
}