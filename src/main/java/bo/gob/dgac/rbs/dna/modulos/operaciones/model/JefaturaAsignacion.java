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
    name = "jefatura_asignaciones",
    schema = "rbs_fusion1"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JefaturaAsignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "orden_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "jefatura_asignaciones_orden_id_fkey")
    )
    private OrdenInspeccion ordenInspeccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "estado_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "jefatura_asignaciones_estado_id_fkey")
    )
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "jefe_usuario_rol_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "jefatura_asignaciones_jefe_usuario_rol_id_fkey")
    )
    private UsuarioRol jefeUsuarioRol;

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