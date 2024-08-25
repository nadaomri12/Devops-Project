package backend.stageproject.Services;

import backend.stageproject.Dto.ProcessDto;
import backend.stageproject.Entity.*;

import backend.stageproject.Entity.Process;
import backend.stageproject.Repository.ProcessRepository;
import backend.stageproject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class ProcessService {
    @Autowired
    private final ProcessRepository processRepository;
    private final UserRepository userRepository;


    public Process addProcess(ProcessDto processDto) {
        User pilot = userRepository.findByEmail(processDto.getPilot())
                .orElseThrow(() -> new IllegalArgumentException("process not found with id: " + processDto.getPilot()));


        Process process =new Process();
        process.setTitle(processDto.getTitle());
        process.setVersion(processDto.getVersion());
        process.setPilot(pilot);
        process.generateCode();
        return processRepository.save(process);
    }

    public Process getProcess(UUID id) {
        Optional<Process> processOptional =processRepository.findById(id);
        // Renvoie l'utilisateur trouvé s'il existe, sinon renvoie null
        return processOptional.orElse(null);    }
    public Page<Process> getProcessPage(PageRequest pageRequest) {
        return processRepository.findAll(pageRequest);
    }

    public List<Process> getProcess() {

        return processRepository.findAll();
    }






    public boolean deleteProcess(UUID id) {
        boolean exist=processRepository.existsById(id);

        if (exist){
            processRepository.deleteById(id);
        }else {
            throw new IllegalStateException(
                    "Process with id "+id+" does not exist "
            );
        };
        return exist;
    }


    public Process updateProcess(UUID id, @NotNull ProcessDto processDto) {
        Process process = processRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("process not found with id: " + id));

        User pilot = userRepository.findByEmail(processDto.getPilot()).orElseThrow(() -> new IllegalArgumentException("pilot not found with id: " + id));

        if (pilot == null) {
            throw new IllegalArgumentException("Pilot not found with name: " + processDto.getPilot());
        }

        process.setTitle(processDto.getTitle());
        process.setVersion(processDto.getVersion());
        process.setPilot(pilot);
        process.generateCode();

        return processRepository.save(process);
    }
}

