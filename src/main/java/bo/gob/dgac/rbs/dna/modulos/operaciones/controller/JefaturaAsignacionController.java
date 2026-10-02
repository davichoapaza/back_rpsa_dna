package bo.gob.dgac.rbs.dna.modulos.operaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignacionesJefeResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.RespuestaDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.JefaturaAsignacionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/operaciones/flujojefe")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class JefaturaAsignacionController {
	
	@Autowired
private	JefaturaAsignacionService jefaturaAsignacionService;

    @GetMapping("/ordenes-inspeccion/{usuarioId}/{rolId}")                                                      
    public ResponseEntity<RespuestaDto<List<AsignacionesJefeResponseDto>>> obtenerResumenOrdenes(@PathVariable Long usuarioId,@PathVariable Long rolId) {
    	System.out.println("usuarioId"+usuarioId);
    	System.out.println("rolId"+rolId);
        List<AsignacionesJefeResponseDto> lista = jefaturaAsignacionService.obtenerAsignacionesJefeYRol(usuarioId, rolId);
        return ResponseEntity.ok(new RespuestaDto<>(true, "Listado de órdenes obtenido exitosamente", lista));
    }
}
