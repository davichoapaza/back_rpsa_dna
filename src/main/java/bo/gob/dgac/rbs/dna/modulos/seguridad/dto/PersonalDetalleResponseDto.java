package bo.gob.dgac.rbs.dna.modulos.seguridad.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor 

public class PersonalDetalleResponseDto {
	
	private String direccionCodigo;
	private String unidadCodigo;
	private String especialidadNombre;
	private Long personaId;
	private String nombres;
	private String primerApellido;
	private String segundoApellido;
	private String ci;
	private Boolean bloqueado;
	private Long usuarioId;
    /*public PersonalDetalleResponseDto(String direccionCodigo) {
        this.direccionCodigo = direccionCodigo;
    } */
}
/*
private String direccionCodigo;
private String unidadCodigo;
private String especialidadNombre;
private Long personaId;
private String nombres;
private String primerApellido;
private String segundoApellido;
private String ci;
private Boolean bloqueado;
private Long usuarioId;*/