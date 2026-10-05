package bo.gob.dgac.rbs.dna.modulos.seguridad.service;



import java.util.List;

import org.springframework.data.repository.query.Param;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.CambiarPasswordRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto;

public interface UsuarioService {
    void cambiarPassword(String username, CambiarPasswordRequestDto dto);
    
       
    List<UsuarioPersonaResponseDto> obtenerUsuariosPorRolEspecialidadArea(Long rolId,Long idEspecialidad, Long idArea);       
}