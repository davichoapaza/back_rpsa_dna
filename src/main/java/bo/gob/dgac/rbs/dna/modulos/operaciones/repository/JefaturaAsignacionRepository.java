package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JefaturaAsignacionRepository extends JpaRepository<JefaturaAsignacion, Long> {

    @EntityGraph(attributePaths = {"ordenInspeccion", "estado", "jefeUsuarioRol"})
    List<JefaturaAsignacion> findByOrdenInspeccionId(Long ordenId);

    @EntityGraph(attributePaths = {"ordenInspeccion", "estado", "jefeUsuarioRol"})
    Page<JefaturaAsignacion> findByJefeUsuarioRolId(Long jefeUsuarioRolId, Pageable pageable);

    @EntityGraph(attributePaths = {"ordenInspeccion", "estado", "jefeUsuarioRol"})
    Optional<JefaturaAsignacion> findTopByOrdenInspeccionIdOrderByIdDesc(Long ordenId);
}