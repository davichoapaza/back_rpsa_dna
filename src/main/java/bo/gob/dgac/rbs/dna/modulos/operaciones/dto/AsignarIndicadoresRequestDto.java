package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;


import java.util.List;

public class AsignarIndicadoresRequestDto {

    private Long inspectorVigilanciaId;
    private List<Long> indicadoresLugarIds;

    public AsignarIndicadoresRequestDto() {}

    public Long getInspectorVigilanciaId() { return inspectorVigilanciaId; }
    public void setInspectorVigilanciaId(Long inspectorVigilanciaId) { this.inspectorVigilanciaId = inspectorVigilanciaId; }

    public List<Long> getIndicadoresLugarIds() { return indicadoresLugarIds; }
    public void setIndicadoresLugarIds(List<Long> indicadoresLugarIds) { this.indicadoresLugarIds = indicadoresLugarIds; }
}