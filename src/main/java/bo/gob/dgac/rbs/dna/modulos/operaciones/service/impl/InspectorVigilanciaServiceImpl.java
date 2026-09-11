package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.modulos.catalogos.Estado;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.InspectorVigilanciaRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.InspectorVigilanciaResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.maper.InspectorVigilanciaMapper;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.EstadoRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.InspectorVigilanciaRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.JefaturaAsignacionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.InspectorVigilanciaService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspectorVigilanciaServiceImpl implements InspectorVigilanciaService {

    private final InspectorVigilanciaRepository inspectorVigilanciaRepository;
    private final JefaturaAsignacionRepository jefaturaAsignacionRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final InspectorVigilanciaMapper mapper;

    @Override
    @Transactional
    public InspectorVigilanciaResponseDto crear(InspectorVigilanciaRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaAsignacionRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Jefatura de asignación no encontrada con ID: " + dto.getJefaturaId()));

        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + dto.getEstadoId()));

        UsuarioRol inspector = usuarioRolRepository.findById(dto.getInspectorUsuarioRolId())
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioRol del inspector no encontrado con ID: " + dto.getInspectorUsuarioRolId()));

        InspectorVigilancia vigilancia = InspectorVigilancia.builder()
                .jefaturaAsignacion(jefatura)
                .estado(estado)
                .inspectorUsuarioRol(inspector)
                .observacionDevolucion(dto.getObservacionDevolucion())
                .build();

        return mapper.toDto(inspectorVigilanciaRepository.save(vigilancia));
    }

    @Override
    @Transactional
    public InspectorVigilanciaResponseDto actualizarObservacionOEstado(Long id, Long nuevoEstadoId, String observacion) {
        InspectorVigilancia vigilancia = inspectorVigilanciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspección de vigilancia no encontrada con ID: " + id));

        if (nuevoEstadoId != null) {
            Estado nuevoEstado = estadoRepository.findById(nuevoEstadoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + nuevoEstadoId));
            vigilancia.setEstado(nuevoEstado);
        }

        if (observacion != null) {
            vigilancia.setObservacionDevolucion(observacion);
        }

        return mapper.toDto(inspectorVigilanciaRepository.save(vigilancia));
    }

    @Override
    @Transactional(readOnly = true)
    public InspectorVigilanciaResponseDto obtenerPorId(Long id) {
        return inspectorVigilanciaRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Inspección de vigilancia no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InspectorVigilanciaResponseDto> obtenerPorJefaturaId(Long jefaturaId) {
        return inspectorVigilanciaRepository.findByJefaturaAsignacionId(jefaturaId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InspectorVigilanciaResponseDto> obtenerPorInspector(Long inspectorUsuarioRolId, Pageable pageable) {
        return inspectorVigilanciaRepository.findByInspectorUsuarioRolId(inspectorUsuarioRolId, pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        InspectorVigilancia vigilancia = inspectorVigilanciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspección de vigilancia no encontrada con ID: " + id));
        inspectorVigilanciaRepository.delete(vigilancia);
    }
}