package backend.stageproject.Repository;

import backend.stageproject.Entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository  extends JpaRepository<Email, Long> {
}
