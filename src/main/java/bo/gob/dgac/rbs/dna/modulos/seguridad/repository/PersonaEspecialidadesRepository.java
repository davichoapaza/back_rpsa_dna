package bo.gob.dgac.rbs.dna.modulos.seguridad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.PersonalDetalleResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.PersonaEspecialidad;

@Repository
public interface PersonaEspecialidadesRepository extends JpaRepository<PersonaEspecialidad, Long> {
    @Query("SELECT NEW com.rbsdna.dto.PersonalDetalleDto(" +
           "pd.codigo, paru.codigo, pae.nombre, per.id, per.nombres, " +
           "per.primerApellido, per.segundoApellido, per.ci, u.bloqueado, u.id) " +
           "FROM PersonaEspecialidad pes " +
           "JOIN ParamEspecialidad pae ON pes.especialidadId = pae.id " +
           "JOIN ParamUnidad paru ON pae.paraUnidadId = paru.id " +
           "JOIN ParamDirecciones pd ON paru.direccionId = pd.id " +
           "JOIN Persona per ON pes.personaId = per.id " +
           "JOIN Usuario u ON u.personaId = per.id")
    List<PersonalDetalleResponseDto> obtenerDetallePersonal();
}