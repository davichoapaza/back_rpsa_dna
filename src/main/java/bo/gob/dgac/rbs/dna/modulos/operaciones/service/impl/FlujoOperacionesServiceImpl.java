package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.modulos.catalogos.Estado;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignarInspectoresRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignarJefaturasRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.InspectorVigilanciaResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.maper.InspectorVigilanciaMapper;
import bo.gob.dgac.rbs.dna.modulos.operaciones.maper.JefaturaAsignacionMapper;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.HistorialTransicion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.EstadoRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.HistorialTransicionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.InspectorVigilanciaRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.JefaturaAsignacionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.OrdenInspeccionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.FlujoOperacionesService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlujoOperacionesServiceImpl implements FlujoOperacionesService {

    // Nombres o constantes de los estados según tu base de datos
    private static final String ESTADO_INSTRUIDO = "INSTRUIDO";
    private static final String ESTADO_POR_DESIGNAR = "POR DESIGNAR";
    private static final String ESTADO_ASIGNADO = "ASIGNADO";

    private final OrdenInspeccionRepository ordenRepository;
    private final JefaturaAsignacionRepository jefaturaRepository;
    private final InspectorVigilanciaRepository inspectorRepository;
    private final HistorialTransicionRepository historialRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRolRepository usuarioRolRepository;

    private final JefaturaAsignacionMapper jefaturaMapper;
    private final InspectorVigilanciaMapper inspectorMapper;

    /**
     * 1. El Director deriva la Orden de Inspección a uno o varios Jefes (AGA, CNS, ATM)
     */
    @Override
    @Transactional
    public List<JefaturaAsignacionResponseDto> asignarAJefaturas(AsignarJefaturasRequestDto request) {
        // Validar la orden
        OrdenInspeccion orden = ordenRepository.findById(request.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden de inspección no encontrada con ID: " + request.getOrdenId()));

        // Obtener el Director
        UsuarioRol director = usuarioRolRepository.findById(request.getDirectorUsuarioRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Director no encontrado con ID: " + request.getDirectorUsuarioRolId()));

        // Estado inicial en la asignación de jefatura (ej: POR DESIGNAR / INSTRUIDO)
        Estado estadoPorDesignar = estadoRepository.findByNombre(ESTADO_POR_DESIGNAR)
                .orElseGet(() -> estadoRepository.findByNombre(ESTADO_INSTRUIDO)
                .orElseThrow(() -> new ResourceNotFoundException("Estado inicial para jefatura no encontrado")));

        List<JefaturaAsignacionResponseDto> respuesta = new ArrayList<>();

        for (Long jefeId : request.getJefesUsuarioRolIds()) {
            UsuarioRol jefe = usuarioRolRepository.findById(jefeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Jefe de unidad no encontrado con ID: " + jefeId));

            // Guardar en la tabla jefatura_asignaciones
            JefaturaAsignacion jefatura = JefaturaAsignacion.builder()
                    .ordenInspeccion(orden)
                    .estado(estadoPorDesignar)
                    .jefeUsuarioRol(jefe)
                    .build();

            JefaturaAsignacion jefaturaGuardada = jefaturaRepository.save(jefatura);

            // Trazabilidad en historial_transiciones
            HistorialTransicion historial = HistorialTransicion.builder()
                    .ordenInspeccion(orden)
                    .jefaturaAsignacion(jefaturaGuardada)
                    .estadoOrigen(orden.getEstado())
                    .estadoDestino(estadoPorDesignar)
                    .accion("DERIVACION_A_JEFATURA")
                    .usuarioRol(director)
                    .observaciones(request.getObservaciones())
                    .build();

            historialRepository.save(historial);
            respuesta.add(jefaturaMapper.toDto(jefaturaGuardada));
        }

        // Actualizar el estado general de la Orden de Inspección
        orden.setEstado(estadoPorDesignar);
        ordenRepository.save(orden);

        return respuesta;
    }

    /**
     * 2. El Jefe de Unidad asigna a uno o más inspectores (o se auto-asigna)
     */
    @Override
    @Transactional
    public List<InspectorVigilanciaResponseDto> asignarAInspectores(AsignarInspectoresRequestDto request) {
        // Validar la asignación de jefatura
        JefaturaAsignacion jefatura = jefaturaRepository.findById(request.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + request.getJefaturaId()));

        // Obtener el Jefe que ejecuta la acción
        UsuarioRol jefe = usuarioRolRepository.findById(request.getJefeUsuarioRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Jefe no encontrado con ID: " + request.getJefeUsuarioRolId()));

        // Estado destino para los inspectores
        Estado estadoAsignado = estadoRepository.findByNombre(ESTADO_ASIGNADO)
                .orElseThrow(() -> new ResourceNotFoundException("Estado " + ESTADO_ASIGNADO + " no encontrado"));

        List<InspectorVigilanciaResponseDto> respuesta = new ArrayList<>();

        for (Long inspectorId : request.getInspectoresUsuarioRolIds()) {
            UsuarioRol inspector = usuarioRolRepository.findById(inspectorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Inspector no encontrado con ID: " + inspectorId));

            // Guardar en la tabla inspector_vigilancias
            InspectorVigilancia vigilancia = InspectorVigilancia.builder()
                    .jefaturaAsignacion(jefatura)
                    .estado(estadoAsignado)
                    .inspectorUsuarioRol(inspector)
                    .build();

            InspectorVigilancia vigilanciaGuardada = inspectorRepository.save(vigilancia);

            // Trazabilidad en historial_transiciones
            HistorialTransicion historial = HistorialTransicion.builder()
                    .ordenInspeccion(jefatura.getOrdenInspeccion())
                    .jefaturaAsignacion(jefatura)
                    .tarea(vigilanciaGuardada)
                    .estadoOrigen(jefatura.getEstado())
                    .estadoDestino(estadoAsignado)
                    .accion("ASIGNACION_A_INSPECTOR")
                    .usuarioRol(jefe)
                    .observaciones(request.getObservaciones())
                    .build();

            historialRepository.save(historial);
            respuesta.add(inspectorMapper.toDto(vigilanciaGuardada));
        }

        // Actualizar el estado de la asignación del jefe
        jefatura.setEstado(estadoAsignado);
        jefaturaRepository.save(jefatura);

        return respuesta;
    }
}