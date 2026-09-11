package bo.gob.dgac.rbs.dna.modulos.operaciones.maper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import bo.gob.dgac.rbs.dna.config.MapStructConfig;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.HistorialTransicionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.HistorialTransicion;

@Mapper(config = MapStructConfig.class)
public interface HistorialTransicionMapper {

    @Mapping(target = "ordenId", source = "ordenInspeccion.id")
    @Mapping(target = "codigoOrden", source = "ordenInspeccion.codigoOrden")
    @Mapping(target = "jefaturaId", source = "jefaturaAsignacion.id")
    @Mapping(target = "tareaId", source = "tarea.id")
    @Mapping(target = "estadoOrigenId", source = "estadoOrigen.id")
    @Mapping(target = "nombreEstadoOrigen", source = "estadoOrigen.nombre")
    @Mapping(target = "estadoDestinoId", source = "estadoDestino.id")
    @Mapping(target = "nombreEstadoDestino", source = "estadoDestino.nombre")
    @Mapping(target = "usuarioRolId", source = "usuarioRol.id")
    HistorialTransicionResponseDto toDto(HistorialTransicion entity);

    List<HistorialTransicionResponseDto> toDtoList(List<HistorialTransicion> entities);
}