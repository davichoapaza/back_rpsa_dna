package bo.gob.dgac.rbs.dna.modulos.seguridad.service;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.MenuRolResponseDto;
import java.util.List;

public interface MenuService {
    List<MenuRolResponseDto> obtenerMenusPorUsuarioYRol(Long usuarioId, Long rolId);
}