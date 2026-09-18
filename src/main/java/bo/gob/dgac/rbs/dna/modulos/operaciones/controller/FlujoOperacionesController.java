package bo.gob.dgac.rbs.dna.modulos.operaciones.controller;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignarInspectoresRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.AsignarJefaturasRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.InspectorVigilanciaResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.JefaturaAsignacionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.FlujoOperacionesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/operaciones/flujo")
@RequiredArgsConstructor
public class FlujoOperacionesController {

    private final FlujoOperacionesService flujoOperacionesService;

    @PostMapping("/asignar-jefaturas")
    public ResponseEntity<List<JefaturaAsignacionResponseDto>> asignarAJefaturas(
            @Valid @RequestBody AsignarJefaturasRequestDto requestDto) {
        List<JefaturaAsignacionResponseDto> result = flujoOperacionesService.asignarAJefaturas(requestDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/asignar-inspectores")
    public ResponseEntity<List<InspectorVigilanciaResponseDto>> asignarAInspectores(
            @Valid @RequestBody AsignarInspectoresRequestDto requestDto) {
        List<InspectorVigilanciaResponseDto> result = flujoOperacionesService.asignarAInspectores(requestDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}