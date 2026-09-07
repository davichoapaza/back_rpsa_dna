package bo.gob.dgac.rbs.dna.modulos.seguridad.repository;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.MenuRolResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT new bo.gob.dgac.rbs.dna.modulos.seguridad.dto.MenuRolResponseDto(" +
           "m.id, m.ruta, m.icon, m.etiqueta, m.orden, m.activo)" +
           "FROM UsuarioRol ur " +
           "JOIN ur.rol r " +
           "JOIN MenuRol mr ON r.id = mr.rol.id " +
           "JOIN mr.menu m " +
           "WHERE ur.usuario.id = :usuarioId " +
           "AND ur.rol.id = :rolId " +
           "AND ur.activo = 'AC' " +
           "AND mr.activo = 'AC' " +
           "AND r.activo = 'AC' " +
           "AND m.activo = 'AC' " +
           "ORDER BY m.orden ASC")
    List<MenuRolResponseDto> findMenusByUsuarioIdAndRolId(@Param("usuarioId") Long usuarioId, 
                                                           @Param("rolId") Long rolId);
}




