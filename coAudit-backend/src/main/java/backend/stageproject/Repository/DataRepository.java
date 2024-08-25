package backend.stageproject.Repository;

import backend.stageproject.Entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataRepository extends JpaRepository<Data, UUID> {
    Optional<Data> findByDesignation(String designation);

    Data findTopByCodeStartingWithOrderByCodeDesc(String baseCode);

    List<Data> findByTaskDataTaskId(UUID taskId);

}