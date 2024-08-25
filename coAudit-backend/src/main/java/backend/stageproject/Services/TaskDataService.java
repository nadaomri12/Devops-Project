package backend.stageproject.Services;

import backend.stageproject.Entity.Data;
import backend.stageproject.Entity.TaskData;
import backend.stageproject.Entity.TypeData;
import backend.stageproject.Repository.DataRepository;
import backend.stageproject.Repository.TaskDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TaskDataService {
    @Autowired

    final TaskDataRepository taskDataRepository;
    public void generateCode(TaskData taskdata) {

        String baseCode = "T_D-";

        TaskData lastdata = taskDataRepository.findTopByCodeStartingWithOrderByCodeDesc(baseCode);
        String lastCode = lastdata != null ? lastdata.getCode() : baseCode + "0000";

        // Extract the last number from the code
        String lastCodeNumberStr = lastCode.substring(baseCode.length());
        int lastCodeNumber = Integer.parseInt(lastCodeNumberStr);
        int newCodeNumber = lastCodeNumber + 1;

        // Generate the new code
        String newCode = baseCode + String.format("%04d", newCodeNumber);
       taskdata.setCode(newCode);
    }


}
