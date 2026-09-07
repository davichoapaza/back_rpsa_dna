package bo.gob.dgac.rbs.dna.modulos.seguridad.repository;

import bo.gob.dgac.rbs.dna.modulos.seguridad.model.SesionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SesionTokenRepository extends JpaRepository<SesionToken, Long> {

    Optional<SesionToken> findByRefreshTokenAndRevocadoFalse(String refreshToken);

    // Opcional: revocar sesiones activas anteriores del mismo usuario
    void deleteByUsuarioId(Long usuarioId);
}