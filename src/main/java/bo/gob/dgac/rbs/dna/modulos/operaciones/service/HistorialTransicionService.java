package bo.gob.dgac.rbs.dna.modulos.operaciones.service;


import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.HistorialTransicionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.HistorialTransicionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HistorialTransicionService {

    HistorialTransicionResponseDto registrarTransicion(HistorialTransicionRequestDto requestDto);

    HistorialTransicionResponseDto obtenerPorId(Long id);

    List<HistorialTransicionResponseDto> obtenerHistorialPorOrden(Long ordenId);

    Page<HistorialTransicionResponseDto> obtenerHistorialPorOrdenPaginado(Long ordenId, Pageable pageable);
}