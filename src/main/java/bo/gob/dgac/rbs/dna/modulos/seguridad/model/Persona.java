package bo.gob.dgac.rbs.dna.modulos.seguridad.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "personas", schema = "rbs_fusion1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(name = "primer_apellido", nullable = false, length = 100)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 100)
    private String segundoApellido;

    @Column(nullable = false, length = 20, unique = true)
    private String ci;

    @Column(length = 150)
    private String correo;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false, length = 2)
    @Builder.Default
    private String activo = "AC";

    @Column(name = "fecha_registro", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime fechaRegistro;
}