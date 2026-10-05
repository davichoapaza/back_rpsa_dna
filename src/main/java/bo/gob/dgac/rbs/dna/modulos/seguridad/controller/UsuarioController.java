package bo.gob.dgac.rbs.dna.modulos.seguridad.controller;



import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.CambiarPasswordRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RespuestaDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/seguridad/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<RespuestaDto<Void>> cambiarPassword(
            @Valid @RequestBody CambiarPasswordRequestDto dto,
            Authentication authentication) {
        try {
            // Se obtiene el usuario autenticado mediante el Token JWT activo
            String username = authentication.getName();
            usuarioService.cambiarPassword(username, dto);
            
            return ResponseEntity.ok(RespuestaDto.exito("Contraseña actualizada exitosamente", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RespuestaDto.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(RespuestaDto.error("Error al actualizar la contraseña: " + e.getMessage()));
        }
    }
    

    @GetMapping("/rol/{rolId}/especialidad/{idEspecialidad}/area/{idArea}")
    public ResponseEntity<RespuestaDto<List<UsuarioPersonaResponseDto>>> obtenerUsuariosPorRol(
            @PathVariable Long rolId,@PathVariable Long idEspecialidad,@PathVariable Long idArea) {
        try {
            List<UsuarioPersonaResponseDto> usuarios = usuarioService.obtenerUsuariosPorRolEspecialidadArea(rolId, idEspecialidad, idArea);
            return ResponseEntity.ok(RespuestaDto.exito("Lista de usuarios obtenida exitosamente", usuarios));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(RespuestaDto.error("Error al obtener usuarios por rol: " + e.getMessage()));
        }
    }
    
    
}