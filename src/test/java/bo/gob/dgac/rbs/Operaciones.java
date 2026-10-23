/*package bo.gob.dgac.rbs;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
public class Operaciones {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void prubas() {
	}
	@Test
	void testObtenerIdUnidadODireccion() {
	    String jpql = """
	        SELECT CASE WHEN pu.id IS NULL THEN pu.direccion ELSE pu.id END
	        FROM Usuario u 
	        JOIN UsuarioRol ur ON ur.usuario = u
	        JOIN u.persona p
	        JOIN PersonaEspecialidad pe ON pe.persona = p
	        JOIN pe.especialidad pe2
	        JOIN pe2.paramUnidad pu
	        WHERE u.id = :usuarioId
	        GROUP BY pu.id, pu.direccion
	    """;

	    Long resultado = entityManager.createQuery(jpql, Long.class)
	            .setParameter("usuarioId", 1L)
	            .getSingleResult();

	    // Validar el resultado en tu test
	    assertNotNull(resultado);
	}
} */

package bo.gob.dgac.rbs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.PersonalDetalleResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.PersonaEspecialidadesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest(classes = DnaRbsBackendApplication.class)
public class Operaciones {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private PersonaEspecialidadesRepository repository;

    // --- AQUÍ CREAS TU MÉTODO DE PRUEBA ---
    // SELECT NEW bo.gob.dgac.rbs.dna.modulos.seguridad.dto.PersonalDetalleResponseDto(" +
    //"pd.codigo, paru.codigo, pae.nombre, per.id, per.nombres," +
    //"per.primerApellido, per.segundoApellido, per.ci, u.bloqueado, u.id) " +	
    //
    @Test
    void pruebafuncion() {
       /* String jpql = """
            SELECT new bo.gob.dgac.rbs.dna.modulos.seguridad.dto.PersonalDetalleResponseDto(
                pd.codigo, paru.codigo, pae.nombre, per.id, per.nombres,per.primerApellido, per.segundoApellido, per.ci, u.bloqueado, u.id
            )
            FROM PersonaEspecialidad pes 
            JOIN pes.especialidad pae 
            JOIN pae.paramUnidad paru 
            JOIN paru.direccion pd 
            JOIN pes.persona per 
            JOIN Usuario u ON u.persona = per
        """;

        try {
            List<PersonalDetalleResponseDto> resultados = entityManager
                    .createQuery(jpql, PersonalDetalleResponseDto.class)
                    .getResultList();
            System.out.println("=== TAMAÑO DE LA LISTA DTO: " + resultados.size() + " ===");
            if (!resultados.isEmpty()) {
                System.out.println("Dirección obtenida: " + resultados.get(0).getDireccionCodigo());
            }
            assertNotNull(resultados);
            assertEquals(1, resultados.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("La consulta con DTO falló: " + e.getMessage());
        }*/
    	
    	
    	
    	
    }
}