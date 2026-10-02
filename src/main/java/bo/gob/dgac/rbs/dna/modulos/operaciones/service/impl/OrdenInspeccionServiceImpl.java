package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.modulos.catalogos.Estado;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionEstadoResponseDto;
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
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.OrdenInspeccionService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;

@Service("ordenInspeccionServiceImpl")
@RequiredArgsConstructor
public class OrdenInspeccionServiceImpl implements OrdenInspeccionService {

    // Identificadores de Estado en la tabla 'estados'
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

    // --- MÉTODOS AUXILIARES ---

    private Estado obtenerEstadoPorId(Long estadoId) {
        return estadoRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + estadoId));
    }

    private UsuarioRol obtenerUsuarioRolPorId(Long usuarioRolId) {
        return usuarioRolRepository.findById(usuarioRolId)
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioRol no encontrado con ID: " + usuarioRolId));
    }

    private void registrarHistorial(OrdenInspeccion orden, JefaturaAsignacion jefatura, InspectorVigilancia tarea,
                                     Estado origen, Estado destino, String accion, UsuarioRol usuario, String observaciones) {
        HistorialTransicion historial = HistorialTransicion.builder()
                .ordenInspeccion(orden)
                .jefaturaAsignacion(jefatura)
                .tarea(tarea)
                .estadoOrigen(origen)
                .estadoDestino(destino)
                .accion(accion)
                .usuarioRol(usuario)
                .observaciones(observaciones)
                .build();
        historialRepository.save(historial);
    }

    // --- PASO 1: CREACIÓN POR LA DIRECCIÓN (BORRADOR) ---

    @Override
    @Transactional
    public OrdenInspeccionResponseDto crearBorrador(OrdenInspeccionRequestDto dto) {
        if (ordenRepository.existsByCodigoOrden(dto.getCodigoOrden())) {
            throw new IllegalArgumentException("Ya existe una orden registrada con el código: " + dto.getCodigoOrden());
        }

        Estado estadoBorrador = obtenerEstadoPorId(ESTADO_BORRADOR_ID);
        UsuarioRol director = obtenerUsuarioRolPorId(dto.getDirectorUsuarioRolId());

        OrdenInspeccion orden = OrdenInspeccion.builder()
                .codigoOrden(dto.getCodigoOrden())
                .titulo(dto.getTitulo())
                .estado(estadoBorrador)
                .directorUsuarioRol(director)
                .build();

        OrdenInspeccion guardada = ordenRepository.save(orden);
        registrarHistorial(guardada, null, null, null, estadoBorrador, "CREACION_BORRADOR", director, "Orden creada en Borrador");

        return ordenMapper.toDto(guardada);
    }

    // --- PASO 2: INSTRUCCIÓN Y DERIVACIÓN A JEFATURA (INSTRUIDO -> PENDIENTE) ---

    @Override
    @Transactional
    public OrdenInspeccionResponseDto instruirYDerivarAJefes(TransicionFlujoRequestDto dto) {
        OrdenInspeccion orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + dto.getOrdenId()));

        UsuarioRol director = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigen = orden.getEstado();
        Estado estadoInstruido = obtenerEstadoPorId(ESTADO_INSTRUIDO_ID);
        Estado estadoJefePendiente = obtenerEstadoPorId(ESTADO_JEFE_PENDIENTE_ID);

        orden.setEstado(estadoInstruido);
        OrdenInspeccion actualizada = ordenRepository.save(orden);

        if (dto.getDestinatariosUsuarioRolIds() != null) {
            for (Long jefeId : dto.getDestinatariosUsuarioRolIds()) {
                UsuarioRol jefe = obtenerUsuarioRolPorId(jefeId);

                JefaturaAsignacion jefatura = JefaturaAsignacion.builder()
                        .ordenInspeccion(actualizada)
                        .estado(estadoJefePendiente)
                        .jefeUsuarioRol(jefe)
                        .build();

                JefaturaAsignacion jefaturaGuardada = jefaturaRepository.save(jefatura);
                registrarHistorial(actualizada, jefaturaGuardada, null, estadoOrigen, estadoJefePendiente, "DERIVACION_A_JEFE", director, dto.getObservaciones());
            }
        }

        return ordenMapper.toDto(actualizada);
    }

    // --- PASO 3: ACEPTACIÓN POR EL JEFE (POR DESIGNAR) ---

    @Override
    @Transactional
    public void aceptarPorJefe(TransicionFlujoRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + dto.getJefaturaId()));

        UsuarioRol jefe = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigen = jefatura.getEstado();
        Estado estadoPorDesignar = obtenerEstadoPorId(ESTADO_JEFE_POR_DESIGNAR_ID);

        jefatura.setEstado(estadoPorDesignar);
        jefaturaRepository.save(jefatura);

        registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, null, estadoOrigen, estadoPorDesignar, "ACEPTAR_ORDEN_JEFE", jefe, dto.getObservaciones());
    }

    // --- PASO 4: ASIGNACIÓN DEL INSPECTOR (JEFE: ASIGNADO / INSPECTOR: PENDIENTE) ---

    @Override
    @Transactional
    public void asignarAInspectores(TransicionFlujoRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + dto.getJefaturaId()));

        UsuarioRol jefe = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigenJefe = jefatura.getEstado();
        Estado estadoJefeAsignado = obtenerEstadoPorId(ESTADO_JEFE_ASIGNADO_ID);
        Estado estadoInspectorPendiente = obtenerEstadoPorId(ESTADO_INSPECTOR_PENDIENTE_ID);

        jefatura.setEstado(estadoJefeAsignado);
        jefaturaRepository.save(jefatura);

        if (dto.getDestinatariosUsuarioRolIds() != null) {
            for (Long inspectorId : dto.getDestinatariosUsuarioRolIds()) {
                UsuarioRol inspector = obtenerUsuarioRolPorId(inspectorId);

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

    // --- PASO 5: EJECUCIÓN DEL TRABAJO DE CAMPO (EN PROCESO) ---

    @Override
    @Transactional
    public void iniciarTrabajoCampo(TransicionFlujoRequestDto dto) {
        InspectorVigilancia tarea = inspectorRepository.findById(dto.getInspectorVigilanciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea de inspección no encontrada con ID: " + dto.getInspectorVigilanciaId()));

        UsuarioRol inspector = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigen = tarea.getEstado();
        Estado estadoEnProceso = obtenerEstadoPorId(ESTADO_INSPECTOR_EN_PROCESO_ID);

        tarea.setEstado(estadoEnProceso);
        inspectorRepository.save(tarea);

        registrarHistorial(tarea.getJefaturaAsignacion().getOrdenInspeccion(), tarea.getJefaturaAsignacion(), tarea, estadoOrigen, estadoEnProceso, "INICIAR_TRABAJO_CAMPO", inspector, dto.getObservaciones());
    }

    // --- PASO 6: REMISIÓN POR EL INSPECTOR (REMITIDO -> JEFE POR VALIDAR) ---

    @Override
    @Transactional
    public void remitirAporValidarJefe(TransicionFlujoRequestDto dto) {
        InspectorVigilancia tarea = inspectorRepository.findById(dto.getInspectorVigilanciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea de inspección no encontrada con ID: " + dto.getInspectorVigilanciaId()));

        UsuarioRol inspector = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigenTarea = tarea.getEstado();
        Estado estadoRemitido = obtenerEstadoPorId(ESTADO_INSPECTOR_REMITIDO_ID);
        Estado estadoJefePorValidar = obtenerEstadoPorId(ESTADO_JEFE_POR_VALIDAR_ID);

        tarea.setEstado(estadoRemitido);
        inspectorRepository.save(tarea);

        JefaturaAsignacion jefatura = tarea.getJefaturaAsignacion();
        jefatura.setEstado(estadoJefePorValidar);
        jefaturaRepository.save(jefatura);

        registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, tarea, estadoOrigenTarea, estadoJefePorValidar, "REMITIR_PLAN_A_JEFE", inspector, dto.getObservaciones());
    }

    // --- PASO 7a: VALIDACIÓN DE LA JEFATURA (VALIDADO -> DIRECTOR POR APROBAR) ---

    @Override
    @Transactional
    public void validarPorJefe(TransicionFlujoRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + dto.getJefaturaId()));

        UsuarioRol jefe = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigenJefe = jefatura.getEstado();
        Estado estadoJefeValidado = obtenerEstadoPorId(ESTADO_JEFE_VALIDADO_ID);
        Estado estadoDirectorPorAprobar = obtenerEstadoPorId(ESTADO_POR_APROBAR_ID);

        jefatura.setEstado(estadoJefeValidado);
        jefaturaRepository.save(jefatura);

        OrdenInspeccion orden = jefatura.getOrdenInspeccion();
        orden.setEstado(estadoDirectorPorAprobar);
        ordenRepository.save(orden);

        registrarHistorial(orden, jefatura, null, estadoOrigenJefe, estadoDirectorPorAprobar, "VALIDAR_Y_ENVIAR_A_DIRECTOR", jefe, dto.getObservaciones());
    }

    // --- PASO 7b: OBSERVACIÓN POR LA JEFATURA (OBSERVADO) ---

    @Override
    @Transactional
    public void observarPorJefe(TransicionFlujoRequestDto dto) {
        InspectorVigilancia tarea = inspectorRepository.findById(dto.getInspectorVigilanciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea de inspección no encontrada con ID: " + dto.getInspectorVigilanciaId()));

        UsuarioRol jefe = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigenTarea = tarea.getEstado();
        Estado estadoInspectorObservado = obtenerEstadoPorId(ESTADO_INSPECTOR_OBSERVADO_ID);
        Estado estadoJefeObservado = obtenerEstadoPorId(ESTADO_JEFE_OBSERVADO_ID);

        tarea.setEstado(estadoInspectorObservado);
        tarea.setObservacionDevolucion(dto.getObservaciones());
        inspectorRepository.save(tarea);

        JefaturaAsignacion jefatura = tarea.getJefaturaAsignacion();
        jefatura.setEstado(estadoJefeObservado);
        jefaturaRepository.save(jefatura);

        registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, tarea, estadoOrigenTarea, estadoInspectorObservado, "OBSERVAR_INSPECCION", jefe, dto.getObservaciones());
    }

    // --- PASO 8: APROBACIÓN Y CIERRE POR LA DIRECCIÓN (APROBADO) ---

    @Override
    @Transactional
    public OrdenInspeccionResponseDto aprobarPorDirector(TransicionFlujoRequestDto dto) {
        OrdenInspeccion orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + dto.getOrdenId()));

        UsuarioRol director = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigen = orden.getEstado();
        Estado estadoAprobado = obtenerEstadoPorId(ESTADO_APROBADO_ID);

        orden.setEstado(estadoAprobado);
        OrdenInspeccion guardada = ordenRepository.save(orden);

        registrarHistorial(guardada, null, null, estadoOrigen, estadoAprobado, "APROBAR_Y_CERRAR_ORDEN", director, dto.getObservaciones());

        return ordenMapper.toDto(guardada);
    }

    // --- MÉTODOS CONSULTA COMPLEMENTARIOS ---

    @Override
    @Transactional(readOnly = true)
    public OrdenInspeccionResponseDto obtenerPorId(Long id) {
        return ordenRepository.findById(id)
                .map(ordenMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de inspección no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenInspeccionResponseDto> obtenerTodos() {
        return ordenMapper.toDtoList(ordenRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenInspeccionResponseDto> obtenerTodosPaginado(Pageable pageable) {
        return ordenRepository.findAll(pageable)
                .map(ordenMapper::toDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrdenInspeccionEstadoResponseDto> obtenerResumenOrdenes() {
        return ordenRepository.obtenerResumenOrdenesConEstado();
        
    }
    
    
}    


/*package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.OrdenInspeccionService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;

@Service()
@RequiredArgsConstructor
public class OrdenInspeccionServiceImpl implements OrdenInspeccionService {

    // Identificadores de Estado en la tabla 'estados'
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

    // --- MÉTODOS AUXILIARES ---

    private Estado obtenerEstadoPorId(Long estadoId) {
        return estadoRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + estadoId));
    }

    private UsuarioRol obtenerUsuarioRolPorId(Long usuarioRolId) {
        return usuarioRolRepository.findById(usuarioRolId)
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioRol no encontrado con ID: " + usuarioRolId));
    }

    private void registrarHistorial(OrdenInspeccion orden, JefaturaAsignacion jefatura, InspectorVigilancia tarea,
                                     Estado origen, Estado destino, String accion, UsuarioRol usuario, String observaciones) {
        HistorialTransicion historial = HistorialTransicion.builder()
                .ordenInspeccion(orden)
                .jefaturaAsignacion(jefatura)
                .tarea(tarea)
                .estadoOrigen(origen)
                .estadoDestino(destino)
                .accion(accion)
                .usuarioRol(usuario)
                .observaciones(observaciones)
                .build();
        historialRepository.save(historial);
    }

    // --- PASO 1: CREACIÓN POR LA DIRECCIÓN (BORRADOR) ---

    @Override
    @Transactional
    public OrdenInspeccionResponseDto crearBorrador(OrdenInspeccionRequestDto dto) {
        if (ordenRepository.existsByCodigoOrden(dto.getCodigoOrden())) {
            throw new IllegalArgumentException("Ya existe una orden registrada con el código: " + dto.getCodigoOrden());
        }

        Estado estadoBorrador = obtenerEstadoPorId(ESTADO_BORRADOR_ID);
        UsuarioRol director = obtenerUsuarioRolPorId(dto.getDirectorUsuarioRolId());

        OrdenInspeccion orden = OrdenInspeccion.builder()
                .codigoOrden(dto.getCodigoOrden())
                .titulo(dto.getTitulo())
                .estado(estadoBorrador)
                .directorUsuarioRol(director)
                .build();

        OrdenInspeccion guardada = ordenRepository.save(orden);
        registrarHistorial(guardada, null, null, null, estadoBorrador, "CREACION_BORRADOR", director, "Orden creada en Borrador");

        return ordenMapper.toDto(guardada);
    }

    // --- PASO 2: INSTRUCCIÓN Y DERIVACIÓN A JEFATURA (INSTRUIDO -> PENDIENTE) ---

    @Override
    @Transactional
    public OrdenInspeccionResponseDto instruirYDerivarAJefes(TransicionFlujoRequestDto dto) {
        OrdenInspeccion orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + dto.getOrdenId()));

        UsuarioRol director = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigen = orden.getEstado();
        Estado estadoInstruido = obtenerEstadoPorId(ESTADO_INSTRUIDO_ID);
        Estado estadoJefePendiente = obtenerEstadoPorId(ESTADO_JEFE_PENDIENTE_ID);

        orden.setEstado(estadoInstruido);
        OrdenInspeccion actualizada = ordenRepository.save(orden);

        if (dto.getDestinatariosUsuarioRolIds() != null) {
            for (Long jefeId : dto.getDestinatariosUsuarioRolIds()) {
                UsuarioRol jefe = obtenerUsuarioRolPorId(jefeId);

                JefaturaAsignacion jefatura = JefaturaAsignacion.builder()
                        .ordenInspeccion(actualizada)
                        .estado(estadoJefePendiente)
                        .jefeUsuarioRol(jefe)
                        .build();

                JefaturaAsignacion jefaturaGuardada = jefaturaRepository.save(jefatura);
                registrarHistorial(actualizada, jefaturaGuardada, null, estadoOrigen, estadoJefePendiente, "DERIVACION_A_JEFE", director, dto.getObservaciones());
            }
        }

        return ordenMapper.toDto(actualizada);
    }

    // --- PASO 3: ACEPTACIÓN POR EL JEFE (POR DESIGNAR) ---

    @Override
    @Transactional
    public void aceptarPorJefe(TransicionFlujoRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + dto.getJefaturaId()));

        UsuarioRol jefe = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigen = jefatura.getEstado();
        Estado estadoPorDesignar = obtenerEstadoPorId(ESTADO_JEFE_POR_DESIGNAR_ID);

        jefatura.setEstado(estadoPorDesignar);
        jefaturaRepository.save(jefatura);

        registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, null, estadoOrigen, estadoPorDesignar, "ACEPTAR_ORDEN_JEFE", jefe, dto.getObservaciones());
    }

    // --- PASO 4: ASIGNACIÓN DEL INSPECTOR (JEFE: ASIGNADO / INSPECTOR: PENDIENTE) ---

    @Override
    @Transactional
    public void asignarAInspectores(TransicionFlujoRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + dto.getJefaturaId()));

        UsuarioRol jefe = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigenJefe = jefatura.getEstado();
        Estado estadoJefeAsignado = obtenerEstadoPorId(ESTADO_JEFE_ASIGNADO_ID);
        Estado estadoInspectorPendiente = obtenerEstadoPorId(ESTADO_INSPECTOR_PENDIENTE_ID);

        jefatura.setEstado(estadoJefeAsignado);
        jefaturaRepository.save(jefatura);

        if (dto.getDestinatariosUsuarioRolIds() != null) {
            for (Long inspectorId : dto.getDestinatariosUsuarioRolIds()) {
                UsuarioRol inspector = obtenerUsuarioRolPorId(inspectorId);

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

    // --- PASO 5: EJECUCIÓN DEL TRABAJO DE CAMPO (EN PROCESO) ---

    @Override
    @Transactional
    public void iniciarTrabajoCampo(TransicionFlujoRequestDto dto) {
        InspectorVigilancia tarea = inspectorRepository.findById(dto.getInspectorVigilanciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea de inspección no encontrada con ID: " + dto.getInspectorVigilanciaId()));

        UsuarioRol inspector = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigen = tarea.getEstado();
        Estado estadoEnProceso = obtenerEstadoPorId(ESTADO_INSPECTOR_EN_PROCESO_ID);

        tarea.setEstado(estadoEnProceso);
        inspectorRepository.save(tarea);

        registrarHistorial(tarea.getJefaturaAsignacion().getOrdenInspeccion(), tarea.getJefaturaAsignacion(), tarea, estadoOrigen, estadoEnProceso, "INICIAR_TRABAJO_CAMPO", inspector, dto.getObservaciones());
    }

    // --- PASO 6: REMISIÓN POR EL INSPECTOR (REMITIDO -> JEFE POR VALIDAR) ---

    @Override
    @Transactional
    public void remitirAporValidarJefe(TransicionFlujoRequestDto dto) {
        InspectorVigilancia tarea = inspectorRepository.findById(dto.getInspectorVigilanciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea de inspección no encontrada con ID: " + dto.getInspectorVigilanciaId()));

        UsuarioRol inspector = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigenTarea = tarea.getEstado();
        Estado estadoRemitido = obtenerEstadoPorId(ESTADO_INSPECTOR_REMITIDO_ID);
        Estado estadoJefePorValidar = obtenerEstadoPorId(ESTADO_JEFE_POR_VALIDAR_ID);

        tarea.setEstado(estadoRemitido);
        inspectorRepository.save(tarea);

        JefaturaAsignacion jefatura = tarea.getJefaturaAsignacion();
        jefatura.setEstado(estadoJefePorValidar);
        jefaturaRepository.save(jefatura);

        registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, tarea, estadoOrigenTarea, estadoJefePorValidar, "REMITIR_PLAN_A_JEFE", inspector, dto.getObservaciones());
    }

    // --- PASO 7a: VALIDACIÓN DE LA JEFATURA (VALIDADO -> DIRECTOR POR APROBAR) ---

    @Override
    @Transactional
    public void validarPorJefe(TransicionFlujoRequestDto dto) {
        JefaturaAsignacion jefatura = jefaturaRepository.findById(dto.getJefaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de jefatura no encontrada con ID: " + dto.getJefaturaId()));

        UsuarioRol jefe = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigenJefe = jefatura.getEstado();
        Estado estadoJefeValidado = obtenerEstadoPorId(ESTADO_JEFE_VALIDADO_ID);
        Estado estadoDirectorPorAprobar = obtenerEstadoPorId(ESTADO_POR_APROBAR_ID);

        jefatura.setEstado(estadoJefeValidado);
        jefaturaRepository.save(jefatura);

        OrdenInspeccion orden = jefatura.getOrdenInspeccion();
        orden.setEstado(estadoDirectorPorAprobar);
        ordenRepository.save(orden);

        registrarHistorial(orden, jefatura, null, estadoOrigenJefe, estadoDirectorPorAprobar, "VALIDAR_Y_ENVIAR_A_DIRECTOR", jefe, dto.getObservaciones());
    }

    // --- PASO 7b: OBSERVACIÓN POR LA JEFATURA (OBSERVADO) ---

    @Override
    @Transactional
    public void observarPorJefe(TransicionFlujoRequestDto dto) {
        InspectorVigilancia tarea = inspectorRepository.findById(dto.getInspectorVigilanciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea de inspección no encontrada con ID: " + dto.getInspectorVigilanciaId()));

        UsuarioRol jefe = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigenTarea = tarea.getEstado();
        Estado estadoInspectorObservado = obtenerEstadoPorId(ESTADO_INSPECTOR_OBSERVADO_ID);
        Estado estadoJefeObservado = obtenerEstadoPorId(ESTADO_JEFE_OBSERVADO_ID);

        tarea.setEstado(estadoInspectorObservado);
        tarea.setObservacionDevolucion(dto.getObservaciones());
        inspectorRepository.save(tarea);

        JefaturaAsignacion jefatura = tarea.getJefaturaAsignacion();
        jefatura.setEstado(estadoJefeObservado);
        jefaturaRepository.save(jefatura);

        registrarHistorial(jefatura.getOrdenInspeccion(), jefatura, tarea, estadoOrigenTarea, estadoInspectorObservado, "OBSERVAR_INSPECCION", jefe, dto.getObservaciones());
    }

    // --- PASO 8: APROBACIÓN Y CIERRE POR LA DIRECCIÓN (APROBADO) ---

    @Override
    @Transactional
    public OrdenInspeccionResponseDto aprobarPorDirector(TransicionFlujoRequestDto dto) {
        OrdenInspeccion orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + dto.getOrdenId()));

        UsuarioRol director = obtenerUsuarioRolPorId(dto.getUsuarioRolId());
        Estado estadoOrigen = orden.getEstado();
        Estado estadoAprobado = obtenerEstadoPorId(ESTADO_APROBADO_ID);

        orden.setEstado(estadoAprobado);
        OrdenInspeccion guardada = ordenRepository.save(orden);

        registrarHistorial(guardada, null, null, estadoOrigen, estadoAprobado, "APROBAR_Y_CERRAR_ORDEN", director, dto.getObservaciones());

        return ordenMapper.toDto(guardada);
    }

    // --- MÉTODOS CONSULTA COMPLEMENTARIOS ---

    @Override
    @Transactional(readOnly = true)
    public OrdenInspeccionResponseDto obtenerPorId(Long id) {
        return ordenRepository.findById(id)
                .map(ordenMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de inspección no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenInspeccionResponseDto> obtenerTodos() {
        return ordenMapper.toDtoList(ordenRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenInspeccionResponseDto> obtenerTodosPaginado(Pageable pageable) {
        return ordenRepository.findAll(pageable)
                .map(ordenMapper::toDto);
    }
}


/*package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;

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
} */