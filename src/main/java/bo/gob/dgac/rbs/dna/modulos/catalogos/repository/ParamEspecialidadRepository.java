package bo.gob.dgac.rbs.dna.modulos.catalogos.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamEspecialidad;

@Repository
public interface ParamEspecialidadRepository extends JpaRepository<ParamEspecialidad, Integer> {
	
    List<ParamEspecialidad> findByParamUnidadId(Integer unidadId);
}