package bo.gob.dgac.rbs;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto1;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
public class Operaciones {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void prubas() {

		/*
		*@Query("SELECT new bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignacionJefaturaResponseDto(" +
       "ja.id, oi.codigoOrden, e.nombre, ja.fechaAsignacion) " +
       "FROM JefaturaAsignacion ja " +
       "JOIN ja.ordenInspeccion oi " +
       "JOIN ja.estado e " +
       "JOIN ja.jefeUsuarioRol ur " +
       "JOIN ur.usuario u " +
       "WHERE u.id = :usuarioId AND ur.rol.id = :rolId " +
       "ORDER BY ja.id DESC")
		List<AsignacionJefaturaResponseDto> buscarPorUsuarioYRol(
    	@Param("usuarioId") Long usuarioId, 
    	@Param("rolId") Long rolId
        );
		 * 
		 * 
		 * */
		
		//"WHERE ur.rol.id = :rolId AND pe2.id = :especialidadId")   
		String jpql = """
			    SELECT new bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto1(ur.id, p.nombres,p.primerApellido, p.segundoApellido, p.ci)
			    FROM UsuarioRol ur
			    JOIN ur.usuario u
			    JOIN u.persona p
			    JOIN PersonaEspecialidad pe ON pe.persona.id = p.id
			    JOIN pe.especialidad e
			    WHERE ur.rol.id = 3 and e.id=1
			    ORDER BY ur.id DESC
						    """;
		List<UsuarioPersonaResponseDto1> resultados =
	            entityManager
	                .createQuery(jpql, UsuarioPersonaResponseDto1.class)
	                //.setParameter("id")
	                .getResultList();
	        resultados.forEach(System.out::println);
	}
	
}
