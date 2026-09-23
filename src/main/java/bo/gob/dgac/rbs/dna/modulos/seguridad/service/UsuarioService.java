package bo.gob.dgac.rbs.dna.modulos.seguridad.service;



import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.CambiarPasswordRequestDto;

public interface UsuarioService {
    void cambiarPassword(String username, CambiarPasswordRequestDto dto);
}