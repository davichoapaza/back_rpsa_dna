package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;



import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JefaturaAsignacionRequestDto {

    @NotNull(message = "El ID de la Orden de Inspección es obligatorio")
    private Long ordenId;

    @NotNull(message = "El ID del Estado es obligatorio")
    private Long estadoId;

    @NotNull(message = "El ID del UsuarioRol del Jefe es obligatorio")
    private Long jefeUsuarioRolId;
}