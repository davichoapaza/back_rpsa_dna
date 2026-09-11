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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "historial_transiciones",
    schema = "rbs_fusion1"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialTransicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "orden_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "historial_transiciones_orden_id_fkey")
    )
    private OrdenInspeccion ordenInspeccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "jefatura_id", 
        foreignKey = @ForeignKey(name = "historial_transiciones_jefatura_id_fkey")
    )
    private JefaturaAsignacion jefaturaAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "tarea_id", 
        foreignKey = @ForeignKey(name = "historial_transiciones_tarea_id_fkey")
    )
    private InspectorVigilancia tarea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "estado_origen_id", 
        foreignKey = @ForeignKey(name = "historial_transiciones_estado_origen_id_fkey")
    )
    private Estado estadoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "estado_destino_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "historial_transiciones_estado_destino_id_fkey")
    )
    private Estado estadoDestino;

    @Column(name = "accion", nullable = false, length = 100)
    private String accion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "usuario_rol_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "historial_transiciones_usuario_rol_id_fkey")
    )
    private UsuarioRol usuarioRol;

    @Column(name = "observaciones", columnDefinition = "text")
    private String observaciones;

    @Column(name = "fecha_transicion", nullable = false, updatable = false)
    private OffsetDateTime fechaTransicion;

    @PrePersist
    protected void onCreate() {
        this.fechaTransicion = OffsetDateTime.now();
    }
}