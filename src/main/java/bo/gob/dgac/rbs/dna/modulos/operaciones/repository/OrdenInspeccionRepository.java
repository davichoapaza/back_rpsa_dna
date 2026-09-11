package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

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
}