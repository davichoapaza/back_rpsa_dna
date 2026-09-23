package bo.gob.dgac.rbs.dna.modulos.operaciones.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.TransicionFlujoRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.FlujoOrdenInspeccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/operaciones/flujo")
@RequiredArgsConstructor
public class FlujoOrdenInspeccionController {

    private final FlujoOrdenInspeccionService flujoService;

    // 1. Director crea borrador
    @PostMapping("/crear-borrador")
    public ResponseEntity<OrdenInspeccionResponseDto> crearBorrador(
            @Valid @RequestBody OrdenInspeccionRequestDto requestDto) {
        return new ResponseEntity<>(flujoService.crearBorrador(requestDto), HttpStatus.CREATED);
    }

    // 2. Director instruye y deriva a los jefes
    @PostMapping("/instruir-y-derivar")
    public ResponseEntity<OrdenInspeccionResponseDto> instruirYDerivar(
            @Valid @RequestBody TransicionFlujoRequestDto requestDto) {
        return ResponseEntity.ok(flujoService.instruirYDerivarAJefes(requestDto));
    }

    // 3. Jefe acepta la orden
    @PostMapping("/jefe/aceptar")
    public ResponseEntity<Void> aceptarPorJefe(@Valid @RequestBody TransicionFlujoRequestDto requestDto) {
        flujoService.aceptarPorJefe(requestDto);
        return ResponseEntity.ok().build();
    }

    // 4. Jefe asigna a inspectores
    @PostMapping("/jefe/asignar-inspectores")
    public ResponseEntity<Void> asignarAInspectores(@Valid @RequestBody TransicionFlujoRequestDto requestDto) {
        flujoService.asignarAInspectores(requestDto);
        return ResponseEntity.ok().build();
    }

    // 5. Inspector inicia trabajo
    @PostMapping("/inspector/iniciar")
    public ResponseEntity<Void> iniciarTrabajoCampo(@Valid @RequestBody TransicionFlujoRequestDto requestDto) {
        flujoService.iniciarTrabajoCampo(requestDto);
        return ResponseEntity.ok().build();
    }

    // 6. Inspector remite plan al jefe
    @PostMapping("/inspector/remitir")
    public ResponseEntity<Void> remitirAporValidarJefe(@Valid @RequestBody TransicionFlujoRequestDto requestDto) {
        flujoService.remitirAporValidarJefe(requestDto);
        return ResponseEntity.ok().build();
    }

    // 7a. Jefe valida y remite al Director
    @PostMapping("/jefe/validar")
    public ResponseEntity<Void> validarPorJefe(@Valid @RequestBody TransicionFlujoRequestDto requestDto) {
        flujoService.validarPorJefe(requestDto);
        return ResponseEntity.ok().build();
    }

    // 7b. Jefe observa el trabajo del inspector
    @PostMapping("/jefe/observar")
    public ResponseEntity<Void> observarPorJefe(@Valid @RequestBody TransicionFlujoRequestDto requestDto) {
        flujoService.observarPorJefe(requestDto);
        return ResponseEntity.ok().build();
    }

    // 8. Director aprueba y cierra la orden
    @PostMapping("/director/aprobar")
    public ResponseEntity<OrdenInspeccionResponseDto> aprobarPorDirector(
            @Valid @RequestBody TransicionFlujoRequestDto requestDto) {
        return ResponseEntity.ok(flujoService.aprobarPorDirector(requestDto));
    }
}