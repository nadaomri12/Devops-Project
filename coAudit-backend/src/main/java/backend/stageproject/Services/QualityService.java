package backend.stageproject.Services;

import backend.stageproject.Dto.QuallityPolicyDto;
import backend.stageproject.Entity.Process;
import backend.stageproject.Entity.QualityPolicy;
import backend.stageproject.Repository.QualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class QualityService {

    @Autowired
    private final QualityRepository qualityRepository;


    public QualityService(QualityRepository qualityRepository) {
        this.qualityRepository = qualityRepository;
    }

    public QualityPolicy addQP(QuallityPolicyDto QP) {
        QualityPolicy QPtosave =new QualityPolicy();
        QPtosave.setTitle(QP.getTitle());
        QPtosave.generateCode();
        return qualityRepository.save(QPtosave);
    }

    public QualityPolicy updateQP(UUID id, QuallityPolicyDto QP) {
        QualityPolicy QPtoupdate = qualityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PQ not found with id: " + id));

        QPtoupdate.setTitle(QP.getTitle());
        QPtoupdate.generateCode();
        return qualityRepository.save(QPtoupdate);
    }

    public QualityPolicy getQP(UUID id) {
        Optional<QualityPolicy> userOptional =qualityRepository.findById(id);
        // Renvoie l'utilisateur trouvé s'il existe, sinon renvoie null
        return userOptional.orElse(null);    }

    public List<QualityPolicy> getQP() {
        return qualityRepository.findAllByOrderByCreatedAtDesc();
    }
    public void deleteQP(UUID id) {
        boolean exist=qualityRepository.existsById(id);

        if (exist){
            qualityRepository.deleteById(id);
        }else {
            throw new IllegalStateException(
                    "QP with id "+id+" does not exist "
            );
        };
    }


}


