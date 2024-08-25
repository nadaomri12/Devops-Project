package backend.stageproject.Repository;

import backend.stageproject.Entity.Operation;
import backend.stageproject.Entity.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface OperationRepository extends JpaRepository<Operation, UUID> {
     Operation findTopByCodeStartingWithOrderByCodeDesc(String baseCode);

    List<Operation> findByProcessId(UUID processId);

    List<Operation> findByResponsibility(Poste responsibility);

}
