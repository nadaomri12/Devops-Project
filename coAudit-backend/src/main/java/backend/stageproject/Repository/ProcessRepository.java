package backend.stageproject.Repository;


import backend.stageproject.Entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ProcessRepository extends JpaRepository<Process, UUID> {
    Process findByid(UUID id);
}
