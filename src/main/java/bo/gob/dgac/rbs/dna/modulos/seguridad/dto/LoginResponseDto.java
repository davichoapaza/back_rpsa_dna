package bo.gob.dgac.rbs.dna.modulos.seguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private Long id;
    private String username;
    private String nombreCompleto;
    private List<RolResponseDto> roles;
    private String token;          // JWT Access Token (duración corta)
    private String refreshToken;   // Refresh Token guardado en la BD
    
    @Builder.Default
    private String tokenType = "Bearer";
}

