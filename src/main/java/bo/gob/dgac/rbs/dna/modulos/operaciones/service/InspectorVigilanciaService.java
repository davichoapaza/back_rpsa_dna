package bo.gob.dgac.rbs.dna.modulos.operaciones.service;



import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.InspectorVigilanciaRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.InspectorVigilanciaResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InspectorVigilanciaService {

    InspectorVigilanciaResponseDto crear(InspectorVigilanciaRequestDto requestDto);

    InspectorVigilanciaResponseDto actualizarObservacionOEstado(Long id, Long nuevoEstadoId, String observacion);

    InspectorVigilanciaResponseDto obtenerPorId(Long id);

    List<InspectorVigilanciaResponseDto> obtenerPorJefaturaId(Long jefaturaId);

    Page<InspectorVigilanciaResponseDto> obtenerPorInspector(Long inspectorUsuarioRolId, Pageable pageable);

    void eliminar(Long id);
}



