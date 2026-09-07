package bo.gob.dgac.rbs.dna.modulos.seguridad.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "sesiones_tokens", schema = "rbs_fusion1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SesionToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "refresh_token", nullable = false, length = 255)
    private String refreshToken;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "expira_en", nullable = false)
    private OffsetDateTime expiraEn;

    @Column(nullable = false)
    @Builder.Default
    private Boolean revocado = false;

    @Column(name = "fecha_creacion", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime fechaCreacion;
}