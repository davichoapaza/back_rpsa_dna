package bo.gob.dgac.rbs.dna.modulos.operaciones.service;


import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignarIndicadoresRequestDto;
import java.util.List;

public interface InspectorIndicadorLugarService {
    
    void guardarIndicadoresInspector(AsignarIndicadoresRequestDto requestDto);
    void guardarIndicadoresMasivo(List<AsignarIndicadoresRequestDto> listaRequest);
    List<Long> obtenerIndicadoresPorInspector(Long inspectorVigilanciaId);

}