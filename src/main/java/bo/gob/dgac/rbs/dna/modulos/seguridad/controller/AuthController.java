package bo.gob.dgac.rbs.dna.modulos.seguridad.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.common.dto.ApiResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RefreshTokenRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
}
