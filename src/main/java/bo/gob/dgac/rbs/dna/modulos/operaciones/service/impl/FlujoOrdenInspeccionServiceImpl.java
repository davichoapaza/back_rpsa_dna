package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.modulos.catalogos.Estado;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.TransicionFlujoRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.maper.OrdenInspeccionMapper;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.HistorialTransicion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.EstadoRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.HistorialTransicionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.InspectorVigilanciaRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.JefaturaAsignacionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.OrdenInspeccionRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.FlujoOrdenInspeccionService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;

@Service("flujoOrdenInspeccionService")
@RequiredArgsConstructor
public class FlujoOrdenInspeccionServiceImpl implements FlujoOrdenInspeccionService {

    // Mapeo directo con los IDs exactos de la tabla 'estados'
    private static final Long ESTADO_BORRADOR_ID = 1L;
    private static final Long ESTADO_INSTRUIDO_ID = 2L;
    private static final Long ESTADO_POR_APROBAR_ID = 4L;
    private static final Long ESTADO_APROBADO_ID = 5L;
    private static final Long ESTADO_JEFE_PENDIENTE_ID = 8L;
    private static final Long ESTADO_JEFE_POR_DESIGNAR_ID = 9L;
    private static final Long ESTADO_JEFE_ASIGNADO_ID = 10L;
    private static final Long ESTADO_JEFE_POR_VALIDAR_ID = 11L;
    private static final Long ESTADO_JEFE_VALIDADO_ID = 12L;
    private static final Long ESTADO_JEFE_OBSERVADO_ID = 13L;
    private static final Long ESTADO_INSPECTOR_PENDIENTE_ID = 14L;
    private static final Long ESTADO_INSPECTOR_EN_PROCESO_ID = 15L;
    private static final Long ESTADO_INSPECTOR_REMITIDO_ID = 16L;
    private static final Long ESTADO_INSPECTOR_OBSERVADO_ID = 17L;

    private final OrdenInspeccionRepository ordenRepository;
    private final JefaturaAsignacionRepository jefaturaRepository;
    private final InspectorVigilanciaRepository inspectorRepository;
    private final HistorialTransicionRepository historialRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final OrdenInspeccionMapper ordenMapper;

    private Estado getEstado(Long id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + id));
    }

    private UsuarioRol getUsuarioRol(Long id) {
        return usuarioRolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioRol no encontrado con ID: " + id));
    }

    private void registrarHistorial(OrdenInspeccion orden, JefaturaAsignacion jefatura, InspectorVigilancia tarea,
                                     Estado origen, Estado destino, String accion, UsuarioRol usuario, String obs) {
        HistorialTransicion historial = HistorialTransicion.builder()
                .ordenInspeccion(orden)
                .jefaturaAsignacion(jefatura)
                .tarea(tarea)
                .estadoOrigen(origen)
                .estadoDestino(destino)
                .accion(accion)
                .usuarioRol(usuario)
                .observaciones(obs)
                .build();
        historialRepository.save(historial);
    }

    // 1. Creación por la Dirección: BORRADOR (ID: 1)
    @Override
    @Transactional
    public OrdenInspeccionResponseDto crearBorrador(OrdenInspeccionRequestDto dto) {
        if (ordenRepository.existsByCodigoOrden(dto.getCodigoOrden())) {
            throw new IllegalArgumentException("El código de orden '" + dto.getCodigoOrden() + "' ya existe.");
        }

        Estado estadoBorrador = getEstado(ESTADO_BORRADOR_ID);
        UsuarioRol director = getUsuarioRol(dto.getDirectorUsuarioRolId());

        OrdenInspeccion orden = OrdenInspeccion.builder()
                .codigoOrden(dto.getCodigoOrden())
                .titulo(dto.getTitulo())
                .estado(estadoBorrador)
                .directorUsuarioRol(director)
                .build();

        OrdenInspeccion guardada = ordenRepository.save(orden);
        registrarHistorial(guardada, null, null, null, estadoBorrador, "CREACION_BORRADOR", director, "Orden creada en borrador.");

        return ordenMapper.toDto(guardada);
    }

    // 2. Instrucción y Derivación a Jefatura: INSTRUIDO (ID: 2) -> Jefes en PENDIENTE (ID: 8)
    @Override
    @Transactional
    public OrdenInspeccionResponseDto instruirYDerivarAJefes(TransicionFlujoRequestDto dto) {
        OrdenInspeccion orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        UsuarioRol director = getUsuarioRol(dto.getUsuarioRolId());
        Estado estadoOrigen = orden.getEstado();
        Estado estadoInstruido = getEstado(ESTADO_INSTRUIDO_ID);
        Estado estadoJefePendiente = getEstado(ESTADO_JEFE_PENDIENTE_ID);

        orden.setEstado(estadoInstruido);
        OrdenInspeccion actualizada = ordenRepository.save(orden);

        if (dto.getDestinatariosUsuarioRolIds() != null) {
            for (Long jefeId : dto.getDestinatariosUsuarioRolIds()) {
                UsuarioRol jefe = getUsuarioRol(jefeId);

                JefaturaAsignacion jefatura = JefaturaAsignacion.builder()
                        .ordenInspeccion(actualizada)
                        .estado(estadoJefePendiente)
                        .jefeUsuarioRol(jefe)
                        .build();

                JefaturaAsignacion jefaturaGuardada = jefaturaRepository.save(jefatura);
                registrarHistorial(actualizada, jefaturaGuardada, null, estadoOrigen, estadoJefePendiente, "DERIVAR_A_JEFE", director, dto.getObservaciones());
            }
        }

        return ordenMapper.toDto(actualizada);
    }

    // 3. Recepción y Aceptación por el Jefe: POR DESIGNAR (ID: 9)
    @Override
    @Transactional
    public void aceptarPorJefe(TransicionFlujoRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada"));

        UsuarioRol jefe = getUsuarioRol(dto.getUsuarioRolId());
        Estado estadoOrigen = jefatura.getEstado();
        Estado estadoPorDesignar = getEstado(ESTADO_JEFE_POR_DESIGNAR_ID);

        jefatura.setEstado(estadoPorDesignar);
        jefaturaRepository.save(jefatura);

        registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, null, estadoOrigen, estadoPorDesignar, "ACEPTAR_JEFATURA", jefe, dto.getObservaciones());
    }

    // 4. Asignación del Inspector: Jefe en ASIGNADO (ID: 10) / Inspector en PENDIENTE (ID: 14)
    @Override
    @Transactional
    public void asignarAInspectores(TransicionFlujoRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada"));

        UsuarioRol jefe = getUsuarioRol(dto.getUsuarioRolId());
        Estado estadoOrigenJefe = jefatura.getEstado();
        Estado estadoJefeAsignado = getEstado(ESTADO_JEFE_ASIGNADO_ID);
        Estado estadoInspectorPendiente = getEstado(ESTADO_INSPECTOR_PENDIENTE_ID);

        jefatura.setEstado(estadoJefeAsignado);
        jefaturaRepository.save(jefatura);

        if (dto.getDestinatariosUsuarioRolIds() != null) {
            for (Long inspectorId : dto.getDestinatariosUsuarioRolIds()) {
                UsuarioRol inspector = getUsuarioRol(inspectorId);

                InspectorVigilancia vigilancia = InspectorVigilancia.builder()
                        .jefaturaAsignacion(jefatura)
                        .estado(estadoInspectorPendiente)
                        .inspectorUsuarioRol(inspector)
                        .build();

                InspectorVigilancia vigilanciaGuardada = inspectorRepository.save(vigilancia);
                registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, vigilanciaGuardada, estadoOrigenJefe, estadoInspectorPendiente, "ASIGNAR_INSPECTOR", jefe, dto.getObservaciones());
            }
        }
    }

    // 5. Ejecución del Trabajo de Campo: EN PROCESO (ID: 15)
    @Override
    @Transactional
    public void iniciarTrabajoCampo(TransicionFlujoRequestDto dto) {
        InspectorVigilancia tarea = inspectorRepository.findById(dto.getInspectorVigilanciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea de inspección no encontrada"));

        UsuarioRol inspector = getUsuarioRol(dto.getUsuarioRolId());
        Estado estadoOrigen = tarea.getEstado();
        Estado estadoEnProceso = getEstado(ESTADO_INSPECTOR_EN_PROCESO_ID);

        tarea.setEstado(estadoEnProceso);
        inspectorRepository.save(tarea);

        registrarHistorial(tarea.getJefaturaAsignacion().getOrdenInspeccion(), tarea.getJefaturaAsignacion(), tarea, estadoOrigen, estadoEnProceso, "INICIAR_TRABAJO_CAMPO", inspector, dto.getObservaciones());
    }

    // 6. Remisión por el Inspector: REMITIDO (ID: 16) -> Jefe a POR VALIDAR (ID: 11)
    @Override
    @Transactional
    public void remitirAporValidarJefe(TransicionFlujoRequestDto dto) {
        InspectorVigilancia tarea = inspectorRepository.findById(dto.getInspectorVigilanciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea de inspección no encontrada"));

        UsuarioRol inspector = getUsuarioRol(dto.getUsuarioRolId());
        Estado estadoOrigenTarea = tarea.getEstado();
        Estado estadoRemitido = getEstado(ESTADO_INSPECTOR_REMITIDO_ID);
        Estado estadoJefePorValidar = getEstado(ESTADO_JEFE_POR_VALIDAR_ID);

        tarea.setEstado(estadoRemitido);
        inspectorRepository.save(tarea);

        JefaturaAsignacion jefatura = tarea.getJefaturaAsignacion();
        jefatura.setEstado(estadoJefePorValidar);
        jefaturaRepository.save(jefatura);

        registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, tarea, estadoOrigenTarea, estadoJefePorValidar, "REMITIR_PLAN_JEFE", inspector, dto.getObservaciones());
    }

    // 7a. Validación de la Jefatura: VALIDADO (ID: 12) -> Director a POR APROBAR (ID: 4)
    @Override
    @Transactional
    public void validarPorJefe(TransicionFlujoRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada"));

        UsuarioRol jefe = getUsuarioRol(dto.getUsuarioRolId());
        Estado estadoOrigenJefe = jefatura.getEstado();
        Estado estadoJefeValidado = getEstado(ESTADO_JEFE_VALIDADO_ID);
        Estado estadoDirectorPorAprobar = getEstado(ESTADO_POR_APROBAR_ID);

        jefatura.setEstado(estadoJefeValidado);
        jefaturaRepository.save(jefatura);

        OrdenInspeccion orden = jefatura.getOrdenInspeccion();
        orden.setEstado(estadoDirectorPorAprobar);
        ordenRepository.save(orden);

        registrarHistorial(orden, jefatura, null, estadoOrigenJefe, estadoDirectorPorAprobar, "VALIDAR_Y_ENVIAR_DIRECTOR", jefe, dto.getObservaciones());
    }

    // 7b. Observación por la Jefatura: OBSERVADO (ID: 13 para Jefe / ID: 17 para Inspector)
    @Override
    @Transactional
    public void observarPorJefe(TransicionFlujoRequestDto dto) {
        InspectorVigilancia tarea = inspectorRepository.findById(dto.getInspectorVigilanciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea de inspección no encontrada"));

        UsuarioRol jefe = getUsuarioRol(dto.getUsuarioRolId());
        Estado estadoOrigenTarea = tarea.getEstado();
        Estado estadoInspectorObservado = getEstado(ESTADO_INSPECTOR_OBSERVADO_ID);
        Estado estadoJefeObservado = getEstado(ESTADO_JEFE_OBSERVADO_ID);

        tarea.setEstado(estadoInspectorObservado);
        tarea.setObservacionDevolucion(dto.getObservaciones());
        inspectorRepository.save(tarea);

        JefaturaAsignacion jefatura = tarea.getJefaturaAsignacion();
        jefatura.setEstado(estadoJefeObservado);
        jefaturaRepository.save(jefatura);

        registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, tarea, estadoOrigenTarea, estadoInspectorObservado, "OBSERVAR_INSPECCION", jefe, dto.getObservaciones());
    }

    // 8. Aprobación y Cierre por la Dirección: APROBADO (ID: 5)
    @Override
    @Transactional
    public OrdenInspeccionResponseDto aprobarPorDirector(TransicionFlujoRequestDto dto) {
        OrdenInspeccion orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        UsuarioRol director = getUsuarioRol(dto.getUsuarioRolId());
        Estado estadoOrigen = orden.getEstado();
        Estado estadoAprobado = getEstado(ESTADO_APROBADO_ID);

        orden.setEstado(estadoAprobado);
        OrdenInspeccion guardada = ordenRepository.save(orden);

        registrarHistorial(guardada, null, null, estadoOrigen, estadoAprobado, "APROBAR_Y_CERRAR_ORDEN", director, dto.getObservaciones());

        return ordenMapper.toDto(guardada);
    }
}