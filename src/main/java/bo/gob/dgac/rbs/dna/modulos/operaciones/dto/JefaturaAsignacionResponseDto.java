package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;


import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JefaturaAsignacionResponseDto {

    private Long id;
    private Long ordenId;
    private String codigoOrden;
    private String tituloOrden;
    private Long estadoId;
    private String nombreEstado;
    private Long jefeUsuarioRolId;
    private OffsetDateTime fechaAsignacion;
    private OffsetDateTime fechaActualizacion;
}