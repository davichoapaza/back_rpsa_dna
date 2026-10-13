package bo.gob.dgac.rbs.dna.modulos.catalogos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamDirecciones;
import bo.gob.dgac.rbs.dna.modulos.catalogos.repository.ParamDireccionesRepository;

@Service
public class ParamDireccionesService {

    private final ParamDireccionesRepository repository;

    public ParamDireccionesService(ParamDireccionesRepository repository) {
        this.repository = repository;
    }

    public List<ParamDirecciones> findAll() {
        return repository.findAll();
    }

    public Optional<ParamDirecciones> findById(Long id) {
        return repository.findById(id);
    }

    public ParamDirecciones save(ParamDirecciones paramDirecciones) {
        return repository.save(paramDirecciones);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}