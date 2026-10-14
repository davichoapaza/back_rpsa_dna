package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorIndicadorLugar;


@Repository
public interface InspectorIndicadorLugarRepository extends JpaRepository<InspectorIndicadorLugar, Long> {

    @Modifying
    @Query(value = "DELETE FROM rbs_fusion1.inspector_indicadores_lugar WHERE inspector_vigilancia_id = :inspectorVigilanciaId", nativeQuery = true)
    void deleteByInspectorVigilanciaId(@Param("inspectorVigilanciaId") Long inspectorVigilanciaId);
}

