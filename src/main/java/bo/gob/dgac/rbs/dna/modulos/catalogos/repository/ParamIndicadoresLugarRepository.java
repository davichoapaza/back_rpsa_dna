package bo.gob.dgac.rbs.dna.modulos.catalogos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamIndicadoresLugar;

@Repository
public interface ParamIndicadoresLugarRepository extends JpaRepository<ParamIndicadoresLugar, Long> {
	
}