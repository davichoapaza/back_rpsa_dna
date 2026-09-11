package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.modulos.catalogos.Estado;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.maper.JefaturaAsignacionMapper;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.EstadoRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.JefaturaAsignacionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.OrdenInspeccionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.JefaturaAsignacionService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JefaturaAsignacionServiceImpl implements JefaturaAsignacionService {

    private final JefaturaAsignacionRepository jefaturaAsignacionRepository;
    private final OrdenInspeccionRepository ordenInspeccionRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final JefaturaAsignacionMapper mapper;

    @Override
    @Transactional
    public JefaturaAsignacionResponseDto crear(JefaturaAsignacionRequestDto dto) {
        OrdenInspeccion orden = ordenInspeccionRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden de inspección no encontrada con ID: " + dto.getOrdenId()));

        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + dto.getEstadoId()));

        UsuarioRol jefe = usuarioRolRepository.findById(dto.getJefeUsuarioRolId())
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioRol del jefe no encontrado con ID: " + dto.getJefeUsuarioRolId()));

        JefaturaAsignacion asignacion = JefaturaAsignacion.builder()
                .ordenInspeccion(orden)
                .estado(estado)
                .jefeUsuarioRol(jefe)
                .build();

        return mapper.toDto(jefaturaAsignacionRepository.save(asignacion));
    }

    @Override
    @Transactional
    public JefaturaAsignacionResponseDto actualizarEstado(Long id, Long nuevoEstadoId) {
        JefaturaAsignacion asignacion = jefaturaAsignacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + id));

        Estado nuevoEstado = estadoRepository.findById(nuevoEstadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + nuevoEstadoId));

        asignacion.setEstado(nuevoEstado);
        return mapper.toDto(jefaturaAsignacionRepository.save(asignacion));
    }

    @Override
    @Transactional(readOnly = true)
    public JefaturaAsignacionResponseDto obtenerPorId(Long id) {
        return jefaturaAsignacionRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JefaturaAsignacionResponseDto> obtenerPorOrdenId(Long ordenId) {
        return jefaturaAsignacionRepository.findByOrdenInspeccionId(ordenId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JefaturaAsignacionResponseDto> obtenerPorJefe(Long jefeUsuarioRolId, Pageable pageable) {
        return jefaturaAsignacionRepository.findByJefeUsuarioRolId(jefeUsuarioRolId, pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        JefaturaAsignacion asignacion = jefaturaAsignacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + id));
        jefaturaAsignacionRepository.delete(asignacion);
    }
}