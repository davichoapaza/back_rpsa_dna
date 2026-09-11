package bo.gob.dgac.rbs.dna.modulos.operaciones.maper;

import bo.gob.dgac.rbs.dna.config.MapStructConfig;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.InspectorVigilanciaResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface InspectorVigilanciaMapper {

    @Mapping(target = "jefaturaId", source = "jefaturaAsignacion.id")
    @Mapping(target = "ordenId", source = "jefaturaAsignacion.ordenInspeccion.id")
    @Mapping(target = "codigoOrden", source = "jefaturaAsignacion.ordenInspeccion.codigoOrden")
    @Mapping(target = "estadoId", source = "estado.id")
    @Mapping(target = "nombreEstado", source = "estado.nombre")
    @Mapping(target = "inspectorUsuarioRolId", source = "inspectorUsuarioRol.id")
    InspectorVigilanciaResponseDto toDto(InspectorVigilancia entity);
}