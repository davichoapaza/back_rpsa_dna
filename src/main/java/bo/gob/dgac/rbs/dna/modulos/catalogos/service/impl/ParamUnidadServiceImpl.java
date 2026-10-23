package bo.gob.dgac.rbs.dna.modulos.catalogos.service.impl;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamUnidad;
import bo.gob.dgac.rbs.dna.modulos.catalogos.repository.ParamUnidadRepository;
import bo.gob.dgac.rbs.dna.modulos.catalogos.service.ParamUnidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParamUnidadServiceImpl implements ParamUnidadService {

    private final ParamUnidadRepository paramUnidadRepository;

    @Override
    public List<ParamUnidad> listarUnidades() {
        return paramUnidadRepository.findAll();
    }

    @Override
    public Optional<ParamUnidad> obtenerUnidadPorId(Integer id) {
        return paramUnidadRepository.findById(id);
    }
}