package bo.gob.dgac.rbs.dna.modulos.operaciones.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "inspector_indicadores_lugar",
    schema = "rbs_fusion1"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectorIndicadorLugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inspector_vigilancia_id", nullable = false)
    private Long inspectorVigilanciaId;

    @Column(name = "indicador_lugar_id", nullable = false)
    private Long indicadorLugarId;

    @Column(name = "activo", nullable = false, length = 2)
    private String activo = "AC";

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

}