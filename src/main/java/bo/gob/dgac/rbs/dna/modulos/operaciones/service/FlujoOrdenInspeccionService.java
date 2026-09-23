package bo.gob.dgac.rbs.dna.modulos.operaciones.service;

import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.OrdenInspeccionResponseDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.TransicionFlujoRequestDto;

public interface FlujoOrdenInspeccionService {

    // Paso 1: Director crea en BORRADOR (ID: 1)
    OrdenInspeccionResponseDto crearBorrador(OrdenInspeccionRequestDto dto);

    // Paso 2: Director pasa a INSTRUIDO (ID: 2) y deriva a los Jefes (PENDIENTE ID: 8)
    OrdenInspeccionResponseDto instruirYDerivarAJefes(TransicionFlujoRequestDto dto);

    // Paso 3: Jefe Acepta la orden pasando a POR DESIGNAR (ID: 9)
    void aceptarPorJefe(TransicionFlujoRequestDto dto);

    // Paso 4: Jefe Asigna a Inspectores (Jefe: ASIGNADO ID: 10 / Inspector: PENDIENTE ID: 14)
    void asignarAInspectores(TransicionFlujoRequestDto dto);

    // Paso 5: Inspector inicia labores en EN PROCESO (ID: 15)
    void iniciarTrabajoCampo(TransicionFlujoRequestDto dto);

    // Paso 6: Inspector concluye y envía en REMITIDO (ID: 16) / Jefe pasa a POR VALIDAR (ID: 11)
    void remitirAporValidarJefe(TransicionFlujoRequestDto dto);

    // Paso 7a: Jefe Valida y envía al Director en VALIDADO (ID: 12) / Director en POR APROBAR (ID: 4)
    void validarPorJefe(TransicionFlujoRequestDto dto);

    // Paso 7b: Jefe u Observador devuelve la inspección (OBSERVADO ID: 13 / ID: 17)
    void observarPorJefe(TransicionFlujoRequestDto dto);

    // Paso 8: Director aprueba el cierre de la orden en APROBADO (ID: 5)
    OrdenInspeccionResponseDto aprobarPorDirector(TransicionFlujoRequestDto dto);
}