package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignacionesJefeResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;

@Repository
public interface JefaturaAsignacionRepository extends JpaRepository<JefaturaAsignacion, Long> {

	@Query("SELECT new bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignacionesJefeResponseDto(" +
		       "ja.id, oi.codigoOrden, e.nombre, ja.fechaAsignacion) " +
		       "FROM JefaturaAsignacion ja " +
		       "JOIN ja.ordenInspeccion oi " +
		       "JOIN ja.estado e " +
		       "JOIN ja.jefeUsuarioRol ur " +
		       "JOIN ur.usuario u " +
		       "WHERE u.id = :usuarioId AND ur.rol.id = :rolId " +
		       "ORDER BY ja.id DESC")
    List<AsignacionesJefeResponseDto> obtenerAsignacionesJefeYRol(
		    	@Param("usuarioId") Long usuarioId, 
		    	@Param("rolId") Long rolId
		        );

	
	@EntityGraph(attributePaths = {"ordenInspeccion", "estado", "jefeUsuarioRol"})
    List<JefaturaAsignacion> findByOrdenInspeccionId(Long ordenId);

    @EntityGraph(attributePaths = {"ordenInspeccion", "estado", "jefeUsuarioRol"})
    Page<JefaturaAsignacion> findByJefeUsuarioRolId(Long jefeUsuarioRolId, Pageable pageable);

    @EntityGraph(attributePaths = {"ordenInspeccion", "estado", "jefeUsuarioRol"})
    Optional<JefaturaAsignacion> findTopByOrdenInspeccionIdOrderByIdDesc(Long ordenId);
    
    
    
    
    
    
}