package bo.gob.dgac.rbs;



import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignacionesJefeResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.JefaturaAsignacionRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
class JefaturaAsignacionRepositoryTest {
/*
    @Autowired
    private JefaturaAsignacionRepository repository;

    @Test
    @DisplayName("Debería obtener las asignaciones de jefatura por usuarioId y rolId")
    void testObtenerAsignacionesJefe() {

        Long usuarioIdPrueba = 1L;
        Long rolIdPrueba = 2L;
        List<AsignacionesJefeResponseDto> resultado = repository.obtenerAsignacionesJefe(usuarioIdPrueba, rolIdPrueba);
        assertNotNull(resultado, "La lista devuelta no debe ser nula");
        
        System.out.println(">>> Cantidad de asignaciones encontradas: " + resultado.size());
        
        resultado.forEach(item -> {
            System.out.println("ID Asignación: " + item.getId());
            System.out.println("Código Orden: " + item.getCodigoOrden());
            System.out.println("Estado: " + item.getNombre());
            System.out.println("Fecha: " + item.getFechaAsignacion());
            System.out.println("---------------------------------------");
        });
    }*/
}