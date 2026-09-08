package bo.gob.dgac.rbs.dna.modulos.operaciones.model;




import java.time.LocalDateTime;

import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "historial_transiciones")
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
    @JoinColumn(name = "orden_id")
    private OrdenInspeccion ordenInspeccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jefatura_id")
    private JefaturaAsignacion jefaturaAsignacion;

    @Column(name = "tarea_id")
    private Long tareaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_origen_id")
    private Estado estadoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_destino_id", nullable = false)
    private Estado estadoDestino;

    @Column(name = "accion", length = 100)
    private String accion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_rol_id", nullable = false)
    private UsuarioRol usuarioRol;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_transicion")
    private LocalDateTime fechaTransicion;

    @PrePersist
    protected void onCreate() {
        this.fechaTransicion = LocalDateTime.now();
    } 
}