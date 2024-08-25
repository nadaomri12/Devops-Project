package backend.stageproject.Repository;

import backend.stageproject.Entity.Data;
import backend.stageproject.Entity.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface PosteRepository extends JpaRepository<Poste, UUID> {
    Poste findTopByCodeStartingWithOrderByCodeDesc(String baseCode);

    Optional<Poste> findByDesignation(String designation);

    Poste findByid(UUID posteid);

}
