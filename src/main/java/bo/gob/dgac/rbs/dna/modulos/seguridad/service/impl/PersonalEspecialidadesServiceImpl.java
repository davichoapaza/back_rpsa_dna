package bo.gob.dgac.rbs.dna.modulos.seguridad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.PersonalDetalleResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.PersonaEspecialidadesRepository;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.PersonalEspecialidadesService;

@Service
public class PersonalEspecialidadesServiceImpl implements PersonalEspecialidadesService {
  
	 @Autowired 
	 private PersonaEspecialidadesRepository personaEspecialidadesRepository ;
	@Override
	public List<PersonalDetalleResponseDto> listarPersonaDetalle() {
		return personaEspecialidadesRepository.obtenerDetallePersonal();
	}

}
