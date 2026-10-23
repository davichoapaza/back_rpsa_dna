package bo.gob.dgac.rbs.dna.modulos.catalogos.service;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamEspecialidad;

import java.util.List;
import java.util.Optional;

public interface ParamEspecialidadService {
    List<ParamEspecialidad> listarEspecialidades();
    Optional<ParamEspecialidad> obtenerEspecialidadPorId(Integer id);
    List<ParamEspecialidad> listarEspecialidadesPorUnidad(Integer unidadId); 
}
