package bo.gob.dgac.rbs.dna.modulos.seguridad.repository;


import bo.gob.dgac.rbs.dna.modulos.seguridad.model.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Long> {
}