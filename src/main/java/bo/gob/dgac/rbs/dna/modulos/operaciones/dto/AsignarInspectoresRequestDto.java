package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AsignarInspectoresRequestDto {

    @NotNull(message = "El ID de la asignación de jefatura es obligatorio")
    private Long jefaturaId;

    @NotEmpty(message = "Debe asignar al menos un inspector o auto-asignarse")
    private List<Long> inspectoresUsuarioRolIds;

    @NotNull(message = "El ID del Jefe que asigna es obligatorio")
    private Long jefeUsuarioRolId;

    private String observaciones;
}