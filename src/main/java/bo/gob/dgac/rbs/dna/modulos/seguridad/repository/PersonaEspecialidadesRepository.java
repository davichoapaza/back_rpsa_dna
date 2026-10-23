package bo.gob.dgac.rbs.dna.modulos.seguridad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.PersonalDetalleResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.PersonaEspecialidad;


@Repository
public interface PersonaEspecialidadesRepository extends JpaRepository<PersonaEspecialidad, Long> {
    @Query("SELECT NEW bo.gob.dgac.rbs.dna.modulos.seguridad.dto.PersonalDetalleResponseDto(" +
           "pd.codigo, paru.codigo, pae.nombre, per.id, per.nombres," +
           "per.primerApellido, per.segundoApellido, per.ci, u.bloqueado, u.id) " +	
           "FROM PersonaEspecialidad pes " +
           "JOIN ParamEspecialidad pae ON pes.especialidad = pae " +
           "JOIN ParamUnidad paru ON pae.paramUnidad = paru " +
           "JOIN ParamDirecciones pd ON paru.direccion = pd " +
           "JOIN Persona per ON pes.persona = per " +
           "JOIN Usuario u ON u.persona = per")
       List<PersonalDetalleResponseDto> obtenerDetallePersonal();
}




