package bo.gob.dgac.rbs.dna.modulos.seguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPersonaResponseDto {
 private Long id;
 private String nombres;
 private String primerApellido;
 private String segundoApellido;
 private String ci;
  	
}
