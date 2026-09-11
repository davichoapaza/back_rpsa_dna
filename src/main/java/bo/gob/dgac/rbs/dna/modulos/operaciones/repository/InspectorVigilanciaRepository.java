package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectorVigilanciaRepository extends JpaRepository<InspectorVigilancia, Long> {

    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    List<InspectorVigilancia> findByJefaturaAsignacionId(Long jefaturaId);

    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    Page<InspectorVigilancia> findByInspectorUsuarioRolId(Long inspectorUsuarioRolId, Pageable pageable);

    @EntityGraph(attributePaths = {"jefaturaAsignacion", "estado", "inspectorUsuarioRol"})
    Page<InspectorVigilancia> findByEstadoId(Long estadoId, Pageable pageable);
}