package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;


import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HistorialTransicionResponseDto {

    private Long id;
    private Long ordenId;
    private String codigoOrden;
    private Long jefaturaId;
    private Long tareaId;
    private Long estadoOrigenId;
    private String nombreEstadoOrigen;
    private Long estadoDestinoId;
    private String nombreEstadoDestino;
    private String accion;
    private Long usuarioRolId;
    private String observaciones;
    private OffsetDateTime fechaTransicion;
        
    
}

