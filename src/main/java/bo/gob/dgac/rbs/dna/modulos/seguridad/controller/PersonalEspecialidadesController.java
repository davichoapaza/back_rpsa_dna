package bo.gob.dgac.rbs.dna.modulos.seguridad.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.PersonalDetalleResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.PersonalEspecialidadesService;

@RestController
@RequestMapping("/api/v1/seguridad/personal")
public class PersonalEspecialidadesController {
	
	   @Autowired
	    private PersonalEspecialidadesService personalEspecialidadesService;
 
	   @GetMapping("/detalle")
	    public ResponseEntity<List<PersonalDetalleResponseDto>> obtenerPersonalDetalle() {
	        List<PersonalDetalleResponseDto> lista = personalEspecialidadesService.listarPersonaDetalle();
	        return ResponseEntity.ok(lista);
	    }
}
