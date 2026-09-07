package bo.gob.dgac.rbs.dna.modulos.seguridad.controller;

import bo.gob.dgac.rbs.dna.common.dto.ApiResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
