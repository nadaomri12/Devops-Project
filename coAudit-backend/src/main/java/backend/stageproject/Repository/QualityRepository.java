package backend.stageproject.Repository;

import backend.stageproject.Entity.QualityPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QualityRepository extends JpaRepository<QualityPolicy, UUID> {
    QualityPolicy findBytitle(String title);
    List<QualityPolicy> findAllByOrderByCreatedAtDesc();
}
