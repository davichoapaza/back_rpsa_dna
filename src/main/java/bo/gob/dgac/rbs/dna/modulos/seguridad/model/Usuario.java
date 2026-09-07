package bo.gob.dgac.rbs.dna.modulos.seguridad.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "usuarios", schema = "rbs_fusion1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false, unique = true)
    private Persona persona;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 255)
    private String salt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean bloqueado = false;

    @Column(name = "requiere_cambio_password", nullable = false)
    @Builder.Default
    private Boolean requiereCambioPassword = false;

    @Column(name = "ultimo_login")
    private OffsetDateTime ultimoLogin;

    @Column(name = "fecha_creacion", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private OffsetDateTime fechaActualizacion;
}