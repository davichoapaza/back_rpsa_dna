package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;




import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrdenInspeccionResponseDto {

    private Long id;
    private String codigoOrden;
    private String titulo;
    private Long estadoId;
    private String nombreEstado;
    private Long directorUsuarioRolId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}