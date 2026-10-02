package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignarIndicadoresRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorIndicadorLugar;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.InspectorIndicadorLugarRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.InspectorIndicadorLugarService;

@Service
public class InspectorIndicadorLugarServiceImpl implements InspectorIndicadorLugarService {

    private final InspectorIndicadorLugarRepository repository;

    public InspectorIndicadorLugarServiceImpl(InspectorIndicadorLugarRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void guardarIndicadoresInspector(AsignarIndicadoresRequestDto requestDto) {
        // 1. Eliminar asignaciones previas del inspector para evitar duplicados
        repository.deleteByInspectorVigilanciaId(requestDto.getInspectorVigilanciaId());

        // 2. Insertar los nuevos seleccionados
        if (requestDto.getIndicadoresLugarIds() != null && !requestDto.getIndicadoresLugarIds().isEmpty()) {
            List<InspectorIndicadorLugar> nuevos = requestDto.getIndicadoresLugarIds().stream()
                    .map(indicadorId -> new InspectorIndicadorLugar(requestDto.getInspectorVigilanciaId(), indicadorId, indicadorId, null, null))
                    .collect(Collectors.toList());

            repository.saveAll(nuevos);
        }
    }

    @Override
    @Transactional
    public void guardarIndicadoresMasivo(List<AsignarIndicadoresRequestDto> listaRequest) {
        for (AsignarIndicadoresRequestDto dto : listaRequest) {
            guardarIndicadoresInspector(dto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> obtenerIndicadoresPorInspector(Long inspectorVigilanciaId) {
        return repository.findByInspectorVigilanciaIdAndActivo(inspectorVigilanciaId, "AC")
                .stream()
                .map(InspectorIndicadorLugar::getIndicadorLugarId)
                .collect(Collectors.toList());
    }
}