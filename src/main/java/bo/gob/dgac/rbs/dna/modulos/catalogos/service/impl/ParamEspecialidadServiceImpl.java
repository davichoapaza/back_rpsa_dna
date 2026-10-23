package bo.gob.dgac.rbs.dna.modulos.catalogos.service.impl;


import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamEspecialidad;
import bo.gob.dgac.rbs.dna.modulos.catalogos.repository.ParamEspecialidadRepository;
import bo.gob.dgac.rbs.dna.modulos.catalogos.service.ParamEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParamEspecialidadServiceImpl implements ParamEspecialidadService {

    private final ParamEspecialidadRepository paramEspecialidadRepository;
    @Override
    public List<ParamEspecialidad> listarEspecialidades() {
        return paramEspecialidadRepository.findAll();
    }
    @Override
    public Optional<ParamEspecialidad> obtenerEspecialidadPorId(Integer id) {
        return paramEspecialidadRepository.findById(id);
    }
    @Override
    public List<ParamEspecialidad> listarEspecialidadesPorUnidad(Integer unidadId) {
        return paramEspecialidadRepository.findByParamUnidadId(unidadId);
    }
}