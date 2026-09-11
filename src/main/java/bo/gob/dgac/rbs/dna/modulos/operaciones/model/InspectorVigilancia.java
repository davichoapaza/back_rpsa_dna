package bo.gob.dgac.rbs.dna.modulos.operaciones.model;




import java.time.OffsetDateTime;

import bo.gob.dgac.rbs.dna.modulos.catalogos.Estado;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "inspector_vigilancias",
    schema = "rbs_fusion1"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectorVigilancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "jefatura_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "inspector_vigilancias_jefatura_id_fkey")
    )
    private JefaturaAsignacion jefaturaAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "estado_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "inspector_vigilancias_estado_id_fkey")
    )
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "inspector_usuario_rol_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "inspector_vigilancias_inspector_usuario_rol_id_fkey")
    )
    private UsuarioRol inspectorUsuarioRol;

    @Column(name = "observacion_devolucion", columnDefinition = "text")
    private String observacionDevolucion;

    @Column(name = "fecha_asignacion", nullable = false, updatable = false)
    private OffsetDateTime fechaAsignacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private OffsetDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime ahora = OffsetDateTime.now();
        this.fechaAsignacion = ahora;
        this.fechaActualizacion = ahora;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = OffsetDateTime.now();
    }
}