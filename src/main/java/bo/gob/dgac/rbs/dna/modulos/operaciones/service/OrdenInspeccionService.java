package bo.gob.dgac.rbs.dna.modulos.operaciones.service;

import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdenInspeccionService {

    OrdenInspeccion crear(OrdenInspeccion orden, Long estadoId, Long directorUsuarioRolId);

    OrdenInspeccion actualizar(Long id, OrdenInspeccion ordenDetails, Long estadoId, Long directorUsuarioRolId);

    OrdenInspeccion obtenerPorId(Long id);

    OrdenInspeccion obtenerPorCodigo(String codigoOrden);

    List<OrdenInspeccion> obtenerTodos();

    Page<OrdenInspeccion> obtenerTodosPaginado(Pageable pageable);

    Page<OrdenInspeccion> obtenerPorEstado(Long estadoId, Pageable pageable);

    void eliminar(Long id);
}