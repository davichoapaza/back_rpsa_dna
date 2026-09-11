package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistorialTransicionRequestDto {

    @NotNull(message = "El ID de la Orden es obligatorio")
    private Long ordenId;

    private Long jefaturaId;

    private Long tareaId;

    private Long estadoOrigenId;

    @NotNull(message = "El ID del Estado Destino es obligatorio")
    private Long estadoDestinoId;

    @NotBlank(message = "La acción es obligatoria")
    @Size(max = 100, message = "La acción no debe superar los 100 caracteres")
    private String accion;

    @NotNull(message = "El ID del UsuarioRol es obligatorio")
    private Long usuarioRolId;

    private String observaciones;
}