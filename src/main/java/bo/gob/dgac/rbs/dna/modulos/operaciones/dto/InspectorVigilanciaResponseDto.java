package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class InspectorVigilanciaResponseDto {

    private Long id;
    private Long jefaturaId;
    private Long ordenId;
    private String codigoOrden;
    private Long estadoId;
    private String nombreEstado;
    private Long inspectorUsuarioRolId;
    private String observacionDevolucion;
    private OffsetDateTime fechaAsignacion;
    private OffsetDateTime fechaActualizacion;
}