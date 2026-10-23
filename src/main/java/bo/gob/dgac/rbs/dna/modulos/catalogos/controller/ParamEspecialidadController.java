package bo.gob.dgac.rbs.dna.modulos.catalogos.controller;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamEspecialidad;
import bo.gob.dgac.rbs.dna.modulos.catalogos.service.ParamEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seguridad/especialidades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ParamEspecialidadController {

    private final ParamEspecialidadService paramEspecialidadService;

 
    @GetMapping
    public ResponseEntity<List<ParamEspecialidad>> listarEspecialidades() {
        List<ParamEspecialidad> especialidades = paramEspecialidadService.listarEspecialidades();
        return ResponseEntity.ok(especialidades);
    }
    
 
    @GetMapping("/unidad/{unidadId}")
    public ResponseEntity<List<ParamEspecialidad>> listarPorUnidad(@PathVariable Integer unidadId) {
        List<ParamEspecialidad> lista = paramEspecialidadService.listarEspecialidadesPorUnidad(unidadId);
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ParamEspecialidad> obtenerEspecialidadPorId(@PathVariable Integer id) {
        return paramEspecialidadService.obtenerEspecialidadPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}