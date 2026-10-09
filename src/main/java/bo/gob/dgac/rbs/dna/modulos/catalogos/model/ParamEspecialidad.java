package bo.gob.dgac.rbs.dna.modulos.catalogos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "param_especialidades", schema = "rbs_fusion1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParamEspecialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "para_unidad_id")
    private ParamUnidad paramUnidad;
}