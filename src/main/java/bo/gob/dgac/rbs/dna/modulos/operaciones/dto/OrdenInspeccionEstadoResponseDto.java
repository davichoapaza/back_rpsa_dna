package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenInspeccionEstadoResponseDto {
	private String codigoOrden;
    private String nombreEstado;
    private LocalDateTime fechaCreacion;
}
