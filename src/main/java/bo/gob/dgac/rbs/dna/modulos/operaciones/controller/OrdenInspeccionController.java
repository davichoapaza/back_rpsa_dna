package bo.gob.dgac.rbs.dna.modulos.operaciones.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.modulos.operaciones.service.OrdenInspeccionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/operaciones/ordenes-inspeccion")
@RequiredArgsConstructor
public class OrdenInspeccionController {

    private final OrdenInspeccionService ordenInspeccionService;

    /*@PostMapping     
    public ResponseEntity<OrdenInspeccionResponseDto> crearOrden(
            @Valid @RequestBody OrdenInspeccionRequestDto requestDto) {
        OrdenInspeccionResponseDto nuevaOrden = ordenInspeccionService.crearOrden(requestDto);
        return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
    }  de una excepcion no hagas un aregla*/ 
}



