package bo.gob.dgac.rbs.dna.modulos.seguridad.controller;

import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Rol;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seguridad/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RolController {

    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<Rol>> listarRoles() {
        List<Rol> roles = rolService.listarRoles();
        return ResponseEntity.ok(roles);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRolPorId(@PathVariable Long id) {
        return rolService.obtenerRolPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}