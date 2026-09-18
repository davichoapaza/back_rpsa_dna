package bo.gob.dgac.rbs.dna.modulos.operaciones.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.OrdenInspeccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/operaciones/ordenes-inspeccion")
@RequiredArgsConstructor
public class OrdenInspeccionController {

    private final OrdenInspeccionService ordenInspeccionService;

    @PostMapping
    public ResponseEntity<OrdenInspeccionResponseDto> crearOrden(
            @Valid @RequestBody OrdenInspeccionRequestDto requestDto) {
        OrdenInspeccionResponseDto nuevaOrden = ordenInspeccionService.crearOrden(requestDto);
        return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
    }
}



