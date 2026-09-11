package bo.gob.dgac.rbs.dna.modulos.operaciones.service;



import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JefaturaAsignacionService {

    JefaturaAsignacionResponseDto crear(JefaturaAsignacionRequestDto requestDto);

    JefaturaAsignacionResponseDto actualizarEstado(Long id, Long nuevoEstadoId);

    JefaturaAsignacionResponseDto obtenerPorId(Long id);

    List<JefaturaAsignacionResponseDto> obtenerPorOrdenId(Long ordenId);

    Page<JefaturaAsignacionResponseDto> obtenerPorJefe(Long jefeUsuarioRolId, Pageable pageable);

    void eliminar(Long id);
}