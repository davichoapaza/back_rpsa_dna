package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;


import bo.gob.dgac.rbs.dna.modulos.operaciones.model.HistorialTransicion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialTransicionRepository extends JpaRepository<HistorialTransicion, Long> {

    @EntityGraph(attributePaths = {"ordenInspeccion", "jefaturaAsignacion", "tarea", "estadoOrigen", "estadoDestino", "usuarioRol"})
    List<HistorialTransicion> findByOrdenInspeccionIdOrderByFechaTransicionAsc(Long ordenId);

    @EntityGraph(attributePaths = {"ordenInspeccion", "jefaturaAsignacion", "tarea", "estadoOrigen", "estadoDestino", "usuarioRol"})
    Page<HistorialTransicion> findByOrdenInspeccionId(Long ordenId, Pageable pageable);

    @EntityGraph(attributePaths = {"ordenInspeccion", "jefaturaAsignacion", "tarea", "estadoOrigen", "estadoDestino", "usuarioRol"})
    List<HistorialTransicion> findByUsuarioRolId(Long usuarioRolId);
}