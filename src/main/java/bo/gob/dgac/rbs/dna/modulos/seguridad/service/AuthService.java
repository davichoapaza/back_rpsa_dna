package bo.gob.dgac.rbs.dna.modulos.seguridad.service;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto request);
}