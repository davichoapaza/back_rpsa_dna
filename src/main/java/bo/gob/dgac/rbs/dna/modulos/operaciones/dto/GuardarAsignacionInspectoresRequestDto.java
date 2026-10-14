package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
public class GuardarAsignacionInspectoresRequestDto {
    private Long jefaturaId;                  
    private Long estadoId;                    
    private List<InspectorAsignacionItemDto> asignaciones;

    @Data
    @NoArgsConstructor  
    @AllArgsConstructor
    public static class InspectorAsignacionItemDto {   
        private Long inspectorUsuarioRolId;       
        private List<Long> indicadoresSeleccionados; 
    }
}
