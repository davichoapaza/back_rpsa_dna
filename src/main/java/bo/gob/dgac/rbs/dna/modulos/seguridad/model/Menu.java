package bo.gob.dgac.rbs.dna.modulos.seguridad.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "menus", schema = "rbs_fusion1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, unique = true)
    private String ruta;

    @Column(length = 50)
    private String icon;

    @Column(nullable = false, length = 100)
    private String etiqueta;

    @Column(nullable = false)
    private Integer orden;

    @Column(nullable = false, length = 2)
    @Builder.Default
    private String activo = "AC";

    @Column(name = "fecha_registro", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion", nullable = false)
    private OffsetDateTime fechaActualizacion;
}