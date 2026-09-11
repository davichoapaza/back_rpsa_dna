package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InspectorVigilanciaRequestDto {

    @NotNull(message = "El ID de la Jefatura de Asignación es obligatorio")
    private Long jefaturaId;

    @NotNull(message = "El ID del Estado es obligatorio")
    private Long estadoId;

    @NotNull(message = "El ID del UsuarioRol del Inspector es obligatorio")
    private Long inspectorUsuarioRolId;

    private String observacionDevolucion;
}