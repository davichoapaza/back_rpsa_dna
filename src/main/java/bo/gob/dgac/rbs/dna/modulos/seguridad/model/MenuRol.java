package bo.gob.dgac.rbs.dna.modulos.seguridad.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "menu_roles", schema = "rbs_fusion1", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"menu_id", "rol_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
  
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(nullable = false, length = 2)
    @Builder.Default
    private String activo = "AC";

    @Column(name = "fecha_asignacion", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime fechaAsignacion;
}