package bo.gob.dgac.rbs.dna.modulos.catalogos.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamDirecciones;

@Repository
public interface ParamDireccionesRepository extends JpaRepository<ParamDirecciones, Long> {
}