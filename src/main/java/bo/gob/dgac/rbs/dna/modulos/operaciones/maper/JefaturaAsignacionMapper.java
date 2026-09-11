package bo.gob.dgac.rbs.dna.modulos.operaciones.maper;



import bo.gob.dgac.rbs.dna.config.MapStructConfig;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface JefaturaAsignacionMapper {

    @Mapping(target = "ordenId", source = "ordenInspeccion.id")
    @Mapping(target = "codigoOrden", source = "ordenInspeccion.codigoOrden")
    @Mapping(target = "tituloOrden", source = "ordenInspeccion.titulo")
    @Mapping(target = "estadoId", source = "estado.id")
    @Mapping(target = "nombreEstado", source = "estado.nombre")
    @Mapping(target = "jefeUsuarioRolId", source = "jefeUsuarioRol.id")
    JefaturaAsignacionResponseDto toDto(JefaturaAsignacion entity);
}