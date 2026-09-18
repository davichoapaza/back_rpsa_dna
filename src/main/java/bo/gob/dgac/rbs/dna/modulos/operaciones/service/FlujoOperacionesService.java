package bo.gob.dgac.rbs.dna.modulos.operaciones.service;

import java.util.List;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignarInspectoresRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignarJefaturasRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.InspectorVigilanciaResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionResponseDto;

public interface FlujoOperacionesService {

    List<JefaturaAsignacionResponseDto> asignarAJefaturas(AsignarJefaturasRequestDto requestDto);

    List<InspectorVigilanciaResponseDto> asignarAInspectores(AsignarInspectoresRequestDto requestDto);
    
}