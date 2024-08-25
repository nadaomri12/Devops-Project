package backend.stageproject.Services;

import backend.stageproject.Dto.operationDto;
import backend.stageproject.Entity.*;
import backend.stageproject.Entity.Process;
import backend.stageproject.Repository.OperationRepository;
import backend.stageproject.Repository.PosteRepository;
import backend.stageproject.Repository.ProcessRepository;
import backend.stageproject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OperationService {
    private final ProcessRepository processRepository;
private final UserRepository userRepository;
private final OperationRepository operationRepository;
private final PosteRepository posteRepository;
private final PosteService posteService;

    public Operation addOperation(operationDto operationDto) {
        User responsible = userRepository.findByEmail(operationDto.getResponsible())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + operationDto.getResponsible()));

        Process process = processRepository.findById(operationDto.getProcess())
                .orElseThrow(() -> new IllegalArgumentException("Process not found with id: " + operationDto.getProcess()));

        // Handle the Poste responsibility
        Poste poste = posteRepository.findByDesignation(operationDto.getResponsibility())
                .orElseGet(() -> {
                    Poste newPoste = new Poste();
                    newPoste.setDesignation(operationDto.getResponsibility());
                    posteService.generateCode(newPoste);
                    return posteRepository.save(newPoste);
                });
        responsible.setPoste(poste);
        Operation operation = new Operation();
        operation.setDesignation(operationDto.getDesignation());
        operation.setResponsibility(poste);
        operation.setProcess(process);
        operation.setResponsible(responsible);
        generateCode(operation);

        return operationRepository.save(operation);
    }

    public Operation updateoperation(UUID id, operationDto operationdto) {
        Operation operationToUpdate=operationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("objective not found with id: " + id));
        User resopnsable = userRepository.findByEmail(operationdto.getResponsible())
                .orElseThrow(() -> new IllegalArgumentException("process not found with id: " + operationdto.getResponsible()));


        Process process =processRepository.findByid(operationdto.getProcess());
        if (process == null) {
            System.out.println("Process not found for userName: " + operationdto.getProcess());

        }
        Poste poste =posteRepository.findByDesignation(operationdto.getResponsibility()) .orElseThrow(() -> new IllegalArgumentException("poste not found for name: " + operationdto.getResponsibility()));

        operationToUpdate.setDesignation(operationdto.getDesignation());
        operationToUpdate.setResponsibility(poste);
        operationToUpdate.setProcess(process);
        operationToUpdate.setResponsible(resopnsable);
        operationToUpdate.generateCode();


        return operationRepository.save(operationToUpdate);
    }
    public Operation getOperation(UUID id) {
        Optional<Operation> operationOptional =operationRepository.findById(id);

        return operationOptional.orElse(null);    }
    public List<Operation> getOperation() {

        return operationRepository.findAll();
    }
    public List<Operation> getopeartionsByProcessId(UUID processId) {
        return operationRepository.findByProcessId(processId);
    }
    public void deleteOperation(UUID id) {
        boolean exist=operationRepository.existsById(id);

        if (exist){
            operationRepository.deleteById(id);
        }else {
            throw new IllegalStateException(
                    "Op with id "+id+" does not exist "
            );
        };
    }

    public void generateCode(Operation operation) {
        String processCode = operation.getProcess().getCode();
        String baseCode = processCode + "-Op-";

       Operation lastoperation = operationRepository.findTopByCodeStartingWithOrderByCodeDesc(baseCode);
        String lastCode = lastoperation != null ? lastoperation.getCode() : baseCode + "0000";

        // Extract the last number from the code
        String lastCodeNumberStr = lastCode.substring(baseCode.length());
        int lastCodeNumber = Integer.parseInt(lastCodeNumberStr);
        int newCodeNumber = lastCodeNumber + 1;

        // Generate the new code
        String newCode = baseCode + String.format("%04d", newCodeNumber);
       operation.setCode(newCode);
    }

}
