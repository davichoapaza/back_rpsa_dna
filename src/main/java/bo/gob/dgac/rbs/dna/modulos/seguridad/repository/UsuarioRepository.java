
package bo.gob.dgac.rbs.dna.modulos.seguridad.repository;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RolResponseDto;
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

    @Query("SELECT new bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RolResponseDto(r.id, r.codigo) " +
           "FROM UsuarioRol ur " +
           "JOIN ur.rol r " +
           "WHERE ur.usuario.id = :usuarioId " +
           "AND ur.activo = 'AC' " +
           "AND r.activo = 'AC'")
    List<RolResponseDto> findRolesActivosByUsuarioId(@Param("usuarioId") Long usuarioId);
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

