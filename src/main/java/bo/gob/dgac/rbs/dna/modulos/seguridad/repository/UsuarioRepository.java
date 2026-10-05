
package bo.gob.dgac.rbs.dna.modulos.seguridad.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RolResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    @Query("SELECT new bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RolResponseDto(r.id, r.codigo) " +
           "FROM UsuarioRol ur " +
           "JOIN ur.rol r " +
           "WHERE ur.usuario.id = :usuarioId " +
           "AND ur.activo = 'AC' " +
           "AND r.activo = 'AC'")
    List<RolResponseDto> findRolesActivosByUsuarioId(@Param("usuarioId") Long usuarioId);
     
    
    // OBTINE LOS USUARIO DE UN DETERMINADO ROL, ESPECALIDAD Y DE UN AREA Y SI
    // ESPECIALIDAD ES CERO RETORNA TODOS DE ESA AREA. ajustar
    @Query("SELECT new bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto(ur.id, p.nombres,p.primerApellido, p.segundoApellido, p.ci)\n"
    		+ "			    FROM UsuarioRol ur\n"
    		+ "			    JOIN ur.usuario u\n"
    		+ "			    JOIN u.persona p\n"
    		+ "			    JOIN PersonaEspecialidad pe ON pe.persona.id = p.id\n"
    		+ "			    JOIN pe.especialidad e\n"
    		+ "			    JOIN e.paramUnidad pu\n"
    		+ "			    JOIN pu.direccion di\n"
    		+ "			    WHERE ur.rol.id = :rolId  AND di.id=:idArea AND (:idEspecialidad = 0 OR e.id= :idEspecialidad)"
    		+ "			    ORDER BY ur.id DESC")
    List<UsuarioPersonaResponseDto> obtenerUsuariosPorRolEspecialidadArea(@Param("rolId") Long rolId, @Param("idEspecialidad") Long idEspecialidad,@Param("idArea") Long idArea);
    
 
}



















/*
package bo.gob.dgac.rbs.dna.modulos.seguridad.repository;

import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    @Query("SELECT r.codigo FROM UsuarioRol ur " +
           "JOIN ur.rol r " +
           "WHERE ur.usuario.id = :usuarioId " +
           "AND ur.activo = 'AC' " +
           "AND r.activo = 'AC'")
    List<String> findCodigosRolesActivosByUsuarioId(@Param("usuarioId") Long usuarioId);
}

*/

