package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionEstadoResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;

@Repository
public interface OrdenInspeccionRepository extends JpaRepository<OrdenInspeccion, Long> {
    boolean existsByCodigoOrden(String codigoOrden);
    Optional<OrdenInspeccion> findByCodigoOrden(String codigoOrden);
    
    
    @Query("SELECT new bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionEstadoResponseDto(" +
            "oi.id,oi.codigoOrden, e.nombre, oi.fechaCreacion) " +
            "FROM OrdenInspeccion oi JOIN oi.estado e ORDER BY oi.id DESC")
     List<OrdenInspeccionEstadoResponseDto> obtenerResumenOrdenesConEstado();
    
}
