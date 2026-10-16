package bo.gob.dgac.rbs.dna.modulos.operaciones.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bo.gob.dgac.rbs.dna.modulos.catalogos.model.Estado;
import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamIndicadoresLugar;
import bo.gob.dgac.rbs.dna.modulos.operaciones.dto.GuardarAsignacionInspectoresRequestDto;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorIndicadorLugar;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.InspectorVigilancia;
import bo.gob.dgac.rbs.dna.modulos.operaciones.model.JefaturaAsignacion;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.InspectorIndicadorLugarRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.repository.InspectorVigilanciaRepository;
import bo.gob.dgac.rbs.dna.modulos.operaciones.service.InspectorIndicadorLugarService;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;

@Service
public class InspectorIndicadorLugarServiceImpl implements InspectorIndicadorLugarService {

    @Autowired
    private InspectorVigilanciaRepository inspectorVigilanciaRepository;

    @Autowired
    private InspectorIndicadorLugarRepository indicadorLugarRepository;

	@Override
	public void procesarAsignacionMasiva(GuardarAsignacionInspectoresRequestDto request) {
	   for(var itemDto : request.getAsignaciones()) {
		  Long usuarioRolId = itemDto.getInspectorUsuarioRolId();
		  List<Long> indicadoresIds= itemDto.getIndicadoresSeleccionados();

           //InspectorVigilancia vigilancia = inspectorVigilanciaRepository
		 //         .findByInspectorUsuarioRolId(usuarioRolId)
		  //        .orElse(null);


		 // if (vigilancia == null) {
		      InspectorVigilancia nueva = new InspectorVigilancia();
		      
		      UsuarioRol usuarioRol = new UsuarioRol();
		      usuarioRol.setId(usuarioRolId);
		      nueva.setInspectorUsuarioRol(usuarioRol);

		      JefaturaAsignacion jefatura = new JefaturaAsignacion();
		      jefatura.setId(request.getJefaturaId());
		      nueva.setJefaturaAsignacion(jefatura);

		      Estado estado = new Estado();
		      estado.setId(request.getEstadoId() != null ? request.getEstadoId() : 1L);
		      nueva.setEstado(estado);

		      //vigilancia = inspectorVigilanciaRepository.save(nueva);
		 // }
		  
          //Long vigilanciaId = vigilancia.getId();
		      Long vigilanciaId =   inspectorVigilanciaRepository.save(nueva).getId();
          //PASO 2: Limpiar los indicadores/lugares anteriores asignados a esta vigilancia
          //indicadorLugarRepository.deleteByInspectorVigilanciaId(vigilanciaId);
       // PASO 3: Guardar los nuevos lugares seleccionados en la tabla hija (inspector_indicadores_lugar)
          if (indicadoresIds != null && !indicadoresIds.isEmpty()) {
        	  List<InspectorIndicadorLugar> listaNuevos = new ArrayList<>();  
          
        	  for (Long indicadorId : indicadoresIds) {
        		  InspectorIndicadorLugar item = new InspectorIndicadorLugar();

                  InspectorVigilancia vRef = new InspectorVigilancia();
                  vRef.setId(vigilanciaId);
                //item.setInspectorVigilancia(vRef);
                  item.setInspectorVigilanciaId(vigilanciaId);

                  ParamIndicadoresLugar pRef = new ParamIndicadoresLugar();
                  pRef.setId(indicadorId);
                  //item.setIndicadorLugar(pRef);
                  item.setIndicadorLugarId(indicadorId);

                  item.setActivo("AC");
                  // Si tu entidad mapea fecha_registro mediante @PrePersist, puedes omitir esta línea o asignarla directamente
                  //item.setFechaRegistro(OffsetDateTime.now());
                  //item.setFechaRegistro(OffsetDateTime.now());

                  listaNuevos.add(item);
              }

              indicadorLugarRepository.saveAll(listaNuevos);
          
	   }
	}
	}	
}
	
	
	