package bo.gob.dgac.rbs.dna.modulos.operaciones.service;


import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.TransicionFlujoRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdenInspeccionService {

    // =========================================================================
    // FLUJO DE TRANSICIONES Y ESTADOS
    // =========================================================================

    /**
     * Paso 1: El Director crea la orden de inspección en estado BORRADOR (ID: 1).
     */
    OrdenInspeccionResponseDto crearBorrador(OrdenInspeccionRequestDto dto);

    /**
     * Paso 2: El Director formaliza la orden a INSTRUIDO (ID: 2) y la deriva
     * a las jefaturas correspondientes en estado PENDIENTE (ID: 8).
     */
    OrdenInspeccionResponseDto instruirYDerivarAJefes(TransicionFlujoRequestDto dto);

    /**
     * Paso 3: El Jefe de Unidad acepta la orden asignada pasando a POR DESIGNAR (ID: 9).
     */
    void aceptarPorJefe(TransicionFlujoRequestDto dto);

    /**
     * Paso 4: El Jefe de Unidad asigna inspectores.
     * En Jefe pasa a ASIGNADO (ID: 10) y en Inspector pasa a PENDIENTE (ID: 14).
     */
    void asignarAInspectores(TransicionFlujoRequestDto dto);

    /**
     * Paso 5: El Inspector inicia el trabajo de campo cambiando a EN PROCESO (ID: 15).
     */
    void iniciarTrabajoCampo(TransicionFlujoRequestDto dto);

    /**
     * Paso 6: El Inspector concluye y remite en estado REMITIDO (ID: 16).
     * La tarea de la jefatura cambia a POR VALIDAR (ID: 11).
     */
    void remitirAporValidarJefe(TransicionFlujoRequestDto dto);

    /**
     * Paso 7a: El Jefe aprueba/valida la propuesta pasando a VALIDADO (ID: 12)
     * y remite al Director en estado POR APROBAR (ID: 4).
     */
    void validarPorJefe(TransicionFlujoRequestDto dto);

    /**
     * Paso 7b: El Jefe o la instancia revisora devuelven con observaciones en estado
     * OBSERVADO (ID: 13 / ID: 17).
     */
    void observarPorJefe(TransicionFlujoRequestDto dto);

    /**
     * Paso 8: El Director otorga el visto bueno final cerrando el flujo en APROBADO (ID: 5).
     */
    OrdenInspeccionResponseDto aprobarPorDirector(TransicionFlujoRequestDto dto);

    // =========================================================================
    // CONSULTAS
    // =========================================================================

    /**
     * Obtiene una Orden de Inspección por su ID.
     */
    OrdenInspeccionResponseDto obtenerPorId(Long id);

    /**
     * Obtiene el listado completo de Ordenes de Inspección.
     */
    List<OrdenInspeccionResponseDto> obtenerTodos();

    /**
     * Obtiene el listado paginado de Ordenes de Inspección.
     */
    Page<OrdenInspeccionResponseDto> obtenerTodosPaginado(Pageable pageable);
}


/*import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;

public interface OrdenInspeccionService {

	OrdenInspeccionResponseDto crearOrden(OrdenInspeccionRequestDto requestDto);
	
    OrdenInspeccion crear(OrdenInspeccion orden, Long estadoId, Long directorUsuarioRolId);

    OrdenInspeccion actualizar(Long id, OrdenInspeccion ordenDetails, Long estadoId, Long directorUsuarioRolId);

    OrdenInspeccion obtenerPorId(Long id);

    OrdenInspeccion obtenerPorCodigo(String codigoOrden);

    List<OrdenInspeccion> obtenerTodos();

    Page<OrdenInspeccion> obtenerTodosPaginado(Pageable pageable);

    Page<OrdenInspeccion> obtenerPorEstado(Long estadoId, Pageable pageable);

    void eliminar(Long id);
}*/