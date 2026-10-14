package bo.gob.dgac.rbs.dna.modulos.operaciones.controller;




import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.GuardarAsignacionInspectoresRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.InspectorIndicadorLugarService;




@RestController
@RequestMapping("/api/v1/inspector-indicadores")
@CrossOrigin(origins = "*")
public class InspectorIndicadorLugarController {

    @Autowired
    private InspectorIndicadorLugarService service;
            

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarAsignaciones(@RequestBody GuardarAsignacionInspectoresRequestDto request) {
        try {
            service.procesarAsignacionMasiva(request);
            return ResponseEntity.ok(Map.of(
                "exito", true, 
                "mensaje", "Vigilancias e indicadores de lugar guardados correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false, 
                "mensaje", "Error al procesar asignaciones: " + e.getMessage()
            ));
        }
    }
}


/*
public class InspectorIndicadorLugarController {

}*/


/*
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignarIndicadoresRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.InspectorIndicadorLugarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inspector-indicadores")
@CrossOrigin(origins = "*") 
public class InspectorIndicadorLugarController {

    private final InspectorIndicadorLugarService service;

    public InspectorIndicadorLugarController(InspectorIndicadorLugarService service) {
        this.service = service;
    }

    // Guardar/Actualizar la configuración de indicadores para un inspector
    @PostMapping("/asignar")
    public ResponseEntity<Map<String, Object>> asignarIndicadores(@RequestBody AsignarIndicadoresRequestDto request) {
        service.procesarAsignacionMasiva(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("exito", true);
        response.put("mensaje", "Indicadores de lugar asignados correctamente al inspector.");
        return ResponseEntity.ok(response);
    }

    //Guardar/Actualizar la configuración completa de la orden (todos los inspectores del modal)
    @PostMapping("/asignar-masivo")
    public ResponseEntity<Map<String, Object>> asignarIndicadoresMasivo(@RequestBody List<AsignarIndicadoresRequestDto> listaRequest) {
        service.guardarIndicadoresMasivo(listaRequest);
        
        Map<String, Object> response = new HashMap<>();
        response.put("exito", true);
        response.put("mensaje", "Configuración de indicadores guardada con éxito.");
        return ResponseEntity.ok(response);
    }
    
    

    //Consultar los IDs de indicadores asignados a un inspector específico
    @GetMapping("/inspector/{inspectorVigilanciaId}")
    public ResponseEntity<List<Long>> obtenerPorInspector(@PathVariable Long inspectorVigilanciaId) {
        List<Long> indicadoresIds = service.obtenerIndicadoresPorInspector(inspectorVigilanciaId);
        return ResponseEntity.ok(indicadoresIds);
    }
}
*/