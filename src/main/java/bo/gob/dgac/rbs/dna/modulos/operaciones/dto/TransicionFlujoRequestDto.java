package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransicionFlujoRequestDto {

    @NotNull(message = "El ID del usuario/rol que ejecuta la acción es obligatorio")
    private Long usuarioRolId;

    private Long ordenId;
    private Long jefaturaId;
    private Long inspectorVigilanciaId;

    // Lista de IDs de destinarios cuando se deriva a múltiples jefes o inspectores
    private List<Long> destinatariosUsuarioRolIds;

    private String observaciones;
}