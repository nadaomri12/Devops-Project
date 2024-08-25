package backend.stageproject.Repository;

import backend.stageproject.Entity.Objective;
import backend.stageproject.Entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ObjectiveRepository  extends JpaRepository<Objective, UUID> {
    @Query("SELECT o FROM Objective o WHERE o.process.id = :processId ORDER BY o.createdAt DESC")
    List<Objective> findByProcessIdOrderByCreatedAtDesc(@Param("processId") UUID processId);


    Objective findTopByCodeStartingWithOrderByCodeDesc(String baseCode);
}
