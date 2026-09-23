/*package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdenInspeccionRepository extends JpaRepository<OrdenInspeccion, Long> {

    @EntityGraph(attributePaths = {"estado", "directorUsuarioRol"})
    Optional<OrdenInspeccion> findByCodigoOrden(String codigoOrden);

    boolean existsByCodigoOrden(String codigoOrden);

    @EntityGraph(attributePaths = {"estado", "directorUsuarioRol"})
    Page<OrdenInspeccion> findByEstadoId(Long estadoId, Pageable pageable);

    @EntityGraph(attributePaths = {"estado", "directorUsuarioRol"})
    Page<OrdenInspeccion> findByDirectorUsuarioRolId(Long directorUsuarioRolId, Pageable pageable);
}*/

package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

import bo.gob.dgac.rbs.dna.modulos.operaciones.model.HistorialTransicion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdenInspeccionRepository extends JpaRepository<OrdenInspeccion, Long> {
    boolean existsByCodigoOrden(String codigoOrden);
    Optional<OrdenInspeccion> findByCodigoOrden(String codigoOrden);
}
/*
@Repository
public interface JefaturaAsignacionRepository extends JpaRepository<JefaturaAsignacion, Long> {}

@Repository
public interface InspectorVigilanciaRepository extends JpaRepository<InspectorVigilancia, Long> {}

@Repository
public interface HistorialTransicionRepository extends JpaRepository<HistorialTransicion, Long> {}
*/