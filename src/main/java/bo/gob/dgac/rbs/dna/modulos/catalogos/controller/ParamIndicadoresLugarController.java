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

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamIndicadoresLugar;
import bo.gob.dgac.rbs.dna.modulos.catalogos.service.ParamIndicadoresLugarService;

@RestController
@RequestMapping("/api/indicadores-lugar")
public class ParamIndicadoresLugarController {

    private final ParamIndicadoresLugarService service;

    public ParamIndicadoresLugarController(ParamIndicadoresLugarService service) {
        this.service = service;
    }

    @GetMapping
    public List<ParamIndicadoresLugar> obtenerTodo() {
        return service.buscarTodo();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParamIndicadoresLugar> obtenerPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ParamIndicadoresLugar> crear(@RequestBody ParamIndicadoresLugar paramIndicadoresLugar) {
        return ResponseEntity.ok(service.save(paramIndicadoresLugar));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParamIndicadoresLugar> actualizar(@PathVariable Long id, @RequestBody ParamIndicadoresLugar paramIndicadoresLugar) {
        return service.buscarPorId(id)
                .map(existing -> {
                    existing.setNombreAerodromo(paramIndicadoresLugar.getNombreAerodromo());
                    existing.setActivo(paramIndicadoresLugar.getActivo());
                    return ResponseEntity.ok(service.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.buscarPorId(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}