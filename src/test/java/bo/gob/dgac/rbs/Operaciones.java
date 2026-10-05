package bo.gob.dgac.rbs;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
public class Operaciones {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void prubas() {


		
		//"WHERE ur.rol.id = :rolId AND pe2.id = :especialidadId")   
/*		String jpql = """
			    SELECT new bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto(ur.id, p.nombres,p.primerApellido, p.segundoApellido, p.ci)
			    FROM UsuarioRol ur
			    JOIN ur.usuario u
			    JOIN u.persona p
			    JOIN PersonaEspecialidad pe ON pe.persona.id = p.id
			    JOIN pe.especialidad e
			    WHERE ur.rol.id = 3 and e.id=1
			    ORDER BY ur.id DESC
						    """;*/
		
		String jpql = """
			    SELECT new bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto(ur.id, p.nombres,p.primerApellido, p.segundoApellido, p.ci)
			    FROM UsuarioRol ur
			    JOIN ur.usuario u
			    JOIN u.persona p
			    JOIN PersonaEspecialidad pe ON pe.persona.id = p.id
			    JOIN pe.especialidad e
			    JOIN e.paramUnidad pu
			    JOIN pu.direccion di
			    WHERE ur.rol.id = 3 and e.id=1 AND (:idArea = 0 OR di.id=:idArea)
			    ORDER BY ur.id DESC
						    """;
		
		List<UsuarioPersonaResponseDto> resultados =
	            entityManager
	                .createQuery(jpql, UsuarioPersonaResponseDto.class)
	                //.setParameter("id")
	                .getResultList();
	        resultados.forEach(System.out::println);
	}
	
}
