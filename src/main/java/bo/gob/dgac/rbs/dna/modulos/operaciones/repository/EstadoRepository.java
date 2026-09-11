package bo.gob.dgac.rbs.dna.modulos.operaciones.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.gob.dgac.rbs.dna.modulos.catalogos.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Optional<Estado> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}