package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

import java.util.List;
import java.util.Optional; // <-- 1. Importar Optional

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;

@Repository
public interface InspectorVigilanciaRepository extends JpaRepository<InspectorVigilancia, Long> {

    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    List<InspectorVigilancia> findByJefaturaAsignacionId(Long jefaturaId);

    // 2. Agregar este método (para la búsqueda individual requerida en la asignación)
    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    Optional<InspectorVigilancia> findByInspectorUsuarioRolId(Long inspectorUsuarioRolId);

    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    Page<InspectorVigilancia> findByInspectorUsuarioRolId(Long inspectorUsuarioRolId, Pageable pageable);

    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    Page<InspectorVigilancia> findByEstadoId(Long estadoId, Pageable pageable);
}

/*package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;

@Repository
public interface InspectorVigilanciaRepository extends JpaRepository<InspectorVigilancia, Long> {

    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    List<InspectorVigilancia> findByJefaturaAsignacionId(Long jefaturaId);

    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    Page<InspectorVigilancia> findByInspectorUsuarioRolId(Long inspectorUsuarioRolId, Pageable pageable);

    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    Page<InspectorVigilancia> findByEstadoId(Long estadoId, Pageable pageable);
}*/