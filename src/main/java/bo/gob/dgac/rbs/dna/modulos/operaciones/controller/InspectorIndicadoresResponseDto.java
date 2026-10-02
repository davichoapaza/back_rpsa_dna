package bo.gob.dgac.rbs.dna.modulos.operaciones.controller;

import java.util.List;

public class InspectorIndicadoresResponseDto {


    private Long inspectorVigilanciaId;
    private String nombreInspector;
    private List<Long> indicadoresLugarIds;

    public InspectorIndicadoresResponseDto(Long inspectorVigilanciaId, String nombreInspector, List<Long> indicadoresLugarIds) {
        this.inspectorVigilanciaId = inspectorVigilanciaId;
        this.nombreInspector = nombreInspector;
        this.indicadoresLugarIds = indicadoresLugarIds;
    }

    // Getters y Setters
    public Long getInspectorVigilanciaId() { return inspectorVigilanciaId; }
    public void setInspectorVigilanciaId(Long inspectorVigilanciaId) { this.inspectorVigilanciaId = inspectorVigilanciaId; }

    public String getNombreInspector() { return nombreInspector; }
    public void setNombreInspector(String nombreInspector) { this.nombreInspector = nombreInspector; }

    public List<Long> getIndicadoresLugarIds() { return indicadoresLugarIds; }
    public void setIndicadoresLugarIds(List<Long> indicadoresLugarIds) { this.indicadoresLugarIds = indicadoresLugarIds; }
}