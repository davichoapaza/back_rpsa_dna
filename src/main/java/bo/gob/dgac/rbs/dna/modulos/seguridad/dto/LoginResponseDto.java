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
    private Long  idUnidad;
    private String nombreCompleto;
    private List<RolResponseDto> roles;
    private String token;         
    private String refreshToken;  
    
    @Builder.Default
    private String tokenType = "Bearer";
}

