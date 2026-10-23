package bo.gob.dgac.rbs.dna.modulos.seguridad.service.impl;

import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Rol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.RolRepository;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> obtenerRolPorId(Long id) {
        return rolRepository.findById(id);
    }
}