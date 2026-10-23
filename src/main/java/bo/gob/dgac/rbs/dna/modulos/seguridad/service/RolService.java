package bo.gob.dgac.rbs.dna.modulos.seguridad.service;

import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {
    List<Rol> listarRoles();
    Optional<Rol> obtenerRolPorId(Long id);
}