package bo.gob.dgac.rbs.dna.modulos.operaciones.service;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignacionesJefeResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionResponseDto;

public interface JefaturaAsignacionService {

    JefaturaAsignacionResponseDto crear(JefaturaAsignacionRequestDto requestDto);

    JefaturaAsignacionResponseDto actualizarEstado(Long id, Long nuevoEstadoId);

    JefaturaAsignacionResponseDto obtenerPorId(Long id);
    
   List<AsignacionesJefeResponseDto> obtenerAsignacionesJefeYRol(Long usuarioId,Long rolId );
   
    List<JefaturaAsignacionResponseDto> obtenerPorOrdenId(Long ordenId);

    Page<JefaturaAsignacionResponseDto> obtenerPorJefe(Long jefeUsuarioRolId, Pageable pageable);

    void eliminar(Long id);
}