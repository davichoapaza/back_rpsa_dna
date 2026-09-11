package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenInspeccionRequestDto {

    @NotBlank(message = "El código de orden es obligatorio")
    @Size(max = 50, message = "El código de orden no debe superar los 50 caracteres")
    private String codigoOrden;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no debe superar los 255 caracteres")
    private String titulo;

    @NotNull(message = "El ID de Estado es obligatorio")
    private Long estadoId;

    @NotNull(message = "El ID del Director (UsuarioRol) es obligatorio")
    private Long directorUsuarioRolId;
}