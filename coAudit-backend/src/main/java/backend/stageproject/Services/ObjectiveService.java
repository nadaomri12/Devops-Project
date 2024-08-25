package backend.stageproject.Services;

import backend.stageproject.Dto.ObjectiveDto;
import backend.stageproject.Dto.QuallityPolicyDto;
import backend.stageproject.Entity.Objective;
import backend.stageproject.Entity.Process;
import backend.stageproject.Entity.QualityPolicy;
import backend.stageproject.Entity.User;
import backend.stageproject.Repository.ObjectiveRepository;
import backend.stageproject.Repository.ProcessRepository;
import backend.stageproject.Repository.QualityRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ObjectiveService {
    @Autowired
    final ObjectiveRepository objectiveRepository;
    final QualityRepository qualityRepository;
    private final ProcessRepository processRepository;

    public Objective addObj(ObjectiveDto objectiveDto) {
        QualityPolicy QP= qualityRepository.findBytitle(objectiveDto.getAxe());
        if (QP == null) {
            System.out.println("QualityPolicy not found for this title: " + objectiveDto.getAxe());

        }
        Process process =processRepository.findByid(objectiveDto.getProcess());
        if (process == null) {
            System.out.println("Process not found for userName: " + objectiveDto.getProcess());

        }
        Objective objective=new Objective();
        objective.setAxe(QP);
        objective.setDesignation(objectiveDto.getDesignation());
        objective.setProcess(process);
         generateCode( objective);
        return objectiveRepository.save( objective);
    }

    public Objective updateobj(UUID id, ObjectiveDto objectiveDto) {
        Objective objectiveToUpdate=objectiveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("objective not found with id: " + id));

        QualityPolicy QP= qualityRepository.findBytitle(objectiveDto.getAxe());
        if (QP == null) {
            System.out.println("QualityPolicy not found for this title: " + objectiveDto.getAxe());

        }
        Process process =processRepository.findByid(objectiveDto.getProcess());
        if (process == null) {
            System.out.println("Process not found for userName: " + objectiveDto.getProcess());

        }

        objectiveToUpdate .setAxe(QP);
        objectiveToUpdate.setDesignation(objectiveDto.getDesignation());
        objectiveToUpdate.setProcess(process);
        objectiveToUpdate.generateCode();

        return objectiveRepository.save(objectiveToUpdate);
    }


        public Objective getObj(UUID id) {
        Optional<Objective> objOptional =objectiveRepository.findById(id);

        return objOptional.orElse(null);    }
    public List<Objective> getObjective() {

        return objectiveRepository.findAll();
    }
    public List<Objective> getObjectivesByProcessId(UUID processId) {
        return objectiveRepository.findByProcessIdOrderByCreatedAtDesc(processId);
    }
    public void deleteObjective(UUID id) {
        boolean exist=objectiveRepository.existsById(id);

        if (exist){
            objectiveRepository.deleteById(id);
        }else {
            throw new IllegalStateException(
                    "Obj with id "+id+" does not exist "
            );
        };
    }
     public void generateCode(Objective objective) {
        String processCode = objective.getProcess().getCode();
        String baseCode = processCode + "-Ob-";

        Objective lastObjective = objectiveRepository.findTopByCodeStartingWithOrderByCodeDesc(baseCode);
        String lastCode = lastObjective != null ? lastObjective.getCode() : baseCode + "0000";

        // Extract the last number from the code
        String lastCodeNumberStr = lastCode.substring(baseCode.length());
        int lastCodeNumber = Integer.parseInt(lastCodeNumberStr);
        int newCodeNumber = lastCodeNumber + 1;

        // Generate the new code
        String newCode = baseCode + String.format("%04d", newCodeNumber);
        objective.setCode(newCode);
    }

    }
