package bo.gob.dgac.rbs.dna.modulos.seguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRolResponseDto {

    // Datos del Menú
    private Long menuId;
    private String ruta;
    private String icon;
    private String etiqueta;
    private Integer orden;
    private String menuActivo;

    /*
    private Long rolId;
    private String codigoRol;
    private String nombreRol;

    
    private Long usuarioId;
    private Long usuarioRolId;
    private Long menuRolId;*/
}