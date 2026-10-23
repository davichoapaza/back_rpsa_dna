package bo.gob.dgac.rbs.dna.modulos.catalogos.controller;


import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamUnidad;
import bo.gob.dgac.rbs.dna.modulos.catalogos.service.ParamUnidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seguridad/unidades") 
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class ParamUnidadController {

    private final ParamUnidadService paramUnidadService;

    
    @GetMapping
    public ResponseEntity<List<ParamUnidad>> listarUnidades() {
        List<ParamUnidad> unidades = paramUnidadService.listarUnidades();
        return ResponseEntity.ok(unidades);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<ParamUnidad> obtenerUnidadPorId(@PathVariable Integer id) {
        return paramUnidadService.obtenerUnidadPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}