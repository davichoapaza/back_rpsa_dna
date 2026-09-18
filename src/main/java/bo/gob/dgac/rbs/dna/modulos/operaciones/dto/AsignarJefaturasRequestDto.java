package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AsignarJefaturasRequestDto {

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long ordenId;

    @NotEmpty(message = "Debe especificar al menos un jefe de unidad (AGA, CNS, ATM)")
    private List<Long> jefesUsuarioRolIds;

    @NotNull(message = "El ID del Director que realiza la asignación es obligatorio")
    private Long directorUsuarioRolId;

    private String observaciones;
}