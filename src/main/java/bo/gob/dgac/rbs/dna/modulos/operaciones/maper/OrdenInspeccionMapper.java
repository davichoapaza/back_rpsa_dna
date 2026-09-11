package bo.gob.dgac.rbs.dna.modulos.operaciones.maper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import bo.gob.dgac.rbs.dna.config.MapStructConfig;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.OrdenInspeccion;

@Mapper(config = MapStructConfig.class)
public interface OrdenInspeccionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "directorUsuarioRol", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    OrdenInspeccion toEntity(OrdenInspeccionRequestDto dto);

    
    @Mapping(target = "estadoId", source = "estado.id")
    @Mapping(target = "nombreEstado", source = "estado.nombre")
    @Mapping(target = "directorUsuarioRolId", source = "directorUsuarioRol.id")
    OrdenInspeccionResponseDto toDto(OrdenInspeccion entity);

    List<OrdenInspeccionResponseDto> toDtoList(List<OrdenInspeccion> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "directorUsuarioRol", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void updateEntityFromDto(OrdenInspeccionRequestDto dto, @MappingTarget OrdenInspeccion entity);
}
