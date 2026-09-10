package bo.gob.dgac.rbs.dna.modulos.catalogos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "param_unidades", schema = "rbs_fusion1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParamUnidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 50)
    private String direccion;

    @Column(name = "ansp_oad", length = 50)
    private String anspOad;

    @Column(name = "descripcion_ansp_oad", length = 100)
    private String descripcionAnspOad;
}