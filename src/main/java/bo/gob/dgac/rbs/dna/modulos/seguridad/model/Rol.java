package bo.gob.dgac.rbs.dna.modulos.seguridad.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles", schema = "rbs_fusion1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false, length = 2)
    @Builder.Default
    private String activo = "AC";
}