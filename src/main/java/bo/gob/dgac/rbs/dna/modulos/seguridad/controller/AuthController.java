package bo.gob.dgac.rbs.dna.modulos.seguridad.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.common.dto.ApiResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.CambiarPasswordRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RefreshTokenRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RespuestaDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.AuthService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    private final UsuarioService usuarioService;

    /*public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }*/

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(
            ApiResponseDto.<LoginResponseDto>builder()
                .exito(true)
                .mensaje("Inicio de sesión exitoso")
                .datos(response)
                .build()
        );
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {
        LoginResponseDto respuesta = authService.refreshToken(request);
        
        return ResponseEntity.ok(
            ApiResponseDto.<LoginResponseDto>builder()
                .exito(true)
                .mensaje("Token renovado exitosamente")
                .datos(respuesta)
                .build()
        );
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
    
}
