
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
     
    /*
    SELECT u.id,p.nombres,p.primer_apellido, p.segundo_apellido, p.ci
FROM usuario_roles ur 
JOIN roles r ON ur.rol_id = r.id
JOIN usuarios u ON ur.usuario_id=u.id
JOIN personas p ON u.persona_id =p.id
WHERE p.activo='AC' AND  r.id=2;
	 */
    @Query("SELECT new bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto(" +
    	       "u.id, p.nombres, p.primerApellido, p.segundoApellido, p.ci) " +
    	       "FROM UsuarioRol ur " +
    	       "JOIN ur.usuario u " +
    	       "JOIN u.persona p " +
    	       "JOIN ur.rol r " +
    	       "WHERE p.activo = 'AC' AND r.id = :rolId")
    List<UsuarioPersonaResponseDto> obtenerUsuariosPorRol(@Param("rolId") Long rolId);
    
    
    
    
    
    
    
    
    
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

