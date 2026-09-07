package bo.gob.dgac.rbs.dna.modulos.seguridad.service.impl;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.MenuRolResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.MenuRepository;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MenuRolResponseDto> obtenerMenusPorUsuarioYRol(Long usuarioId, Long rolId) {
        return menuRepository.findMenusByUsuarioIdAndRolId(usuarioId, rolId);
    }
}