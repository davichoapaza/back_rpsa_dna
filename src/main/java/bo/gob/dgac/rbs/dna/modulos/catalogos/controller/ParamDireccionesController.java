package bo.gob.dgac.rbs.dna.modulos.catalogos.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamDirecciones;
import bo.gob.dgac.rbs.dna.modulos.catalogos.service.ParamDireccionesService;

@RestController
@RequestMapping("/api/direcciones")
public class ParamDireccionesController {

    private final ParamDireccionesService service;

    public ParamDireccionesController(ParamDireccionesService service) {
        this.service = service;
    }

    @GetMapping
    public List<ParamDirecciones> obtenerTodos() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParamDirecciones> obtenerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ParamDirecciones> crear(@RequestBody ParamDirecciones paramDirecciones) {
        return ResponseEntity.ok(service.save(paramDirecciones));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParamDirecciones> actualizar(@PathVariable Long id, @RequestBody ParamDirecciones paramDirecciones) {
        return service.findById(id)
                .map(existing -> {
                    existing.setCodigo(paramDirecciones.getCodigo());
                    existing.setDescripcion(paramDirecciones.getDescripcion());
                    return ResponseEntity.ok(service.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}