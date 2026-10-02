package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AsignacionesJefeResponseDto {
  private Long id;
  private String codigoOrden;
   private String nombre;
  private OffsetDateTime fechaAsignacion;
}
