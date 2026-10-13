package bo.gob.dgac.rbs.dna.modulos.catalogos.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamIndicadoresLugar;
import bo.gob.dgac.rbs.dna.modulos.catalogos.repository.ParamIndicadoresLugarRepository;

@Service
public class ParamIndicadoresLugarService {

    private final ParamIndicadoresLugarRepository repository;

    public ParamIndicadoresLugarService(ParamIndicadoresLugarRepository repository) {
        this.repository = repository;
    }

    public List<ParamIndicadoresLugar> buscarTodo() {
        return repository.findAll();
    }

    public Optional<ParamIndicadoresLugar> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public ParamIndicadoresLugar save(ParamIndicadoresLugar paramIndicadoresLugar) {
        return repository.save(paramIndicadoresLugar);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}