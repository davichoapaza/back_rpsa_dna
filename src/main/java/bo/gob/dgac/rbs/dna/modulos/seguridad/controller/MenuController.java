package bo.gob.dgac.rbs.dna.modulos.seguridad.controller;

import bo.gob.dgac.rbs.dna.common.dto.ApiResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.MenuRolResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seguridad/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/usuario/{usuarioId}/rol/{rolId}")
    public ResponseEntity<ApiResponseDto<List<MenuRolResponseDto>>> obtenerMenusPorUsuarioYRol(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId) {
    	
        List<MenuRolResponseDto> menus = menuService.obtenerMenusPorUsuarioYRol(usuarioId, rolId);

        return ResponseEntity.ok(
            ApiResponseDto.<List<MenuRolResponseDto>>builder()
                .exito(true)
                .mensaje("Menús obtenidos exitosamente para el usuario y rol especificados")
                .datos(menus)
                .build()
        );
    }
}

