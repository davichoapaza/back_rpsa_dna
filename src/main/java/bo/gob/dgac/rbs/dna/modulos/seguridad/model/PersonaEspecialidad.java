package bo.gob.dgac.rbs.dna.modulos.seguridad.model;

import bo.gob.dgac.rbs.dna.modulos.catalogos.model.ParamEspecialidad;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "persona_especialidades", schema = "rbs_fusion1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaEspecialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id", nullable = false)
    private ParamEspecialidad especialidad;

    @Column(name = "fecha_registro", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime fechaRegistro;
}