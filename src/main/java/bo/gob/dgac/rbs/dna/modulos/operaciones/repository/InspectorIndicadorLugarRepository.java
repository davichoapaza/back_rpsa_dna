package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorIndicadorLugar;

@Repository
public interface InspectorIndicadorLugarRepository extends JpaRepository<InspectorIndicadorLugar, Long> {

    // Buscar todos los indicadores de un inspector específico
    List<InspectorIndicadorLugar> findByInspectorVigilanciaIdAndActivo(Long inspectorVigilanciaId, String activo);

    // Método para eliminar o desactivar asignaciones anteriores antes de guardar las nuevas (Update de lista)
    @Modifying
    @Query("DELETE FROM InspectorIndicadorLugar i WHERE i.inspectorVigilanciaId = :inspectorVigilanciaId")
    void deleteByInspectorVigilanciaId(@Param("inspectorVigilanciaId") Long inspectorVigilanciaId);
}