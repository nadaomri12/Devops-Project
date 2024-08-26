package backend.stageproject.Services;

import backend.stageproject.Dto.DataDto;
import backend.stageproject.Entity.*;
import backend.stageproject.Repository.DataRepository;
import backend.stageproject.Repository.TaskDataRepository;
import backend.stageproject.Repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DataService {
    @Autowired
    final DataRepository dataRepository;
    final TaskDataRepository taskDataRepository;
    final TaskRepository taskRepository;
    final private TaskDataService taskDataService;

    @Transactional
    public void addData(DataDto dataDto) {
        // Check if the Data entity exists
        Data data = dataRepository.findByDesignation(dataDto.getDesignation())
                .orElseGet(() -> {
                    Data newData = new Data();
                    newData.setDesignation(dataDto.getDesignation());
                    generateCode(newData);
                    return dataRepository.save(newData);
                });

        // Retrieve the associated Task
        Task task = taskRepository.findById(dataDto.getTask())
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + dataDto.getTask()));
        Optional<TaskData> taskData = taskDataRepository.findByTaskIdAndDataIdAndType(task.getId(), data.getId(), dataDto.getType());
        // Check if TaskData with the same type already exists for this Data and Task
        System.out.println(taskData.isPresent());
        if (!taskData.isPresent()) {
            // Create and set up TaskData if it doesn't exist
            TaskData newTaskData = new TaskData();
            newTaskData.setData(data);
            newTaskData.setType(dataDto.getType());
            newTaskData.setTask(task);
           taskDataService.generateCode(newTaskData);
            taskDataRepository.save(newTaskData);
            // Add TaskData to the Task and Data entities
            /// task.getTaskData().add(newTaskData);
            // data.getTaskData().add(newTaskData);
        }

    }

    public List<Data> getDataBytaskId(UUID TaskId) {
        return dataRepository.findByTaskDataTaskId(TaskId);
    }

    public void updateData(UUID id, DataDto dataDto) {
        TaskData existingTaskData =taskDataRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task Data not found with id: " + id));
        // Vérifier si l'entité Data existe par son ID
        Data data = dataRepository.findByDesignation(dataDto.getDesignation())
                .orElseGet(() -> {
                    Data newData = new Data();
                    newData.setDesignation(dataDto.getDesignation());
                    generateCode(newData);
                    return dataRepository.save(newData);
                });

        // Modifier le type de TaskData existant
        existingTaskData.setType(dataDto.getType());
        existingTaskData.setData(data);
        taskDataRepository.save(existingTaskData);


    }

    public Data getData(UUID id) {
        Optional<Data> dataOptional = dataRepository.findById(id);
        return dataOptional.orElse(null);
    }

    public List<Data> getAllData() {
        return dataRepository.findAll();
    }

    public void generateCode(Data data) {

        String baseCode = "D-";

        Data lastdata = dataRepository.findTopByCodeStartingWithOrderByCodeDesc(baseCode);
        String lastCode = lastdata != null ? lastdata.getCode() : baseCode + "0000";

        // Extract the last number from the code
        String lastCodeNumberStr = lastCode.substring(baseCode.length());
        int lastCodeNumber = Integer.parseInt(lastCodeNumberStr);
        int newCodeNumber = lastCodeNumber + 1;

        // Generate the new code
        String newCode = baseCode + String.format("%04d", newCodeNumber);
        data.setCode(newCode);
    }

    public void deleteData(UUID id) {
        boolean exists = taskDataRepository.existsById(id);
        if (exists) {
            taskDataRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Data with id " + id + " does not exist");
        }
    }

    public List<TaskData> getAllTaskData(UUID id) {
        return  taskDataRepository.findAllByTaskId(id);
    }

}