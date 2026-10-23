package bo.gob.dgac.rbs.dna.modulos.catalogos.service;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamUnidad;

import java.util.List;
import java.util.Optional;

public interface ParamUnidadService {
    List<ParamUnidad> listarUnidades();
    Optional<ParamUnidad> obtenerUnidadPorId(Integer id);
}