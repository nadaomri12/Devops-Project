package backend.stageproject.Services;

import backend.stageproject.Dto.DataDto;
import backend.stageproject.Dto.TaskDto;
import backend.stageproject.Entity.*;
import backend.stageproject.Repository.DataRepository;
import backend.stageproject.Repository.OperationRepository;
import backend.stageproject.Repository.TaskDataRepository;
import backend.stageproject.Repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static backend.stageproject.Entity.TypeData.INPUT;

@Service
@AllArgsConstructor
public class TaskService {
    @Autowired
    final TaskRepository taskRepository;
    private final OperationRepository operationRepository;
    private final DataRepository dataRepository;
    private final TaskDataRepository taskDataRepository;
    private DataService dataService;
/*
    public Task addTask(TaskDto taskDto) {
        // Retrieve the associated Operation
        Operation operation = operationRepository.findById(taskDto.getOperation())
                .orElseThrow(() -> new IllegalArgumentException("Operation not found for id: " + taskDto.getOperation()));

        // Create and set up the Task
        Task task = new Task();
        task.setDesignation(taskDto.getDesignation());
        task.setOperation(operation);
        generateCode(task);  // Ensure the task code is generated

        // Initialize list for TaskData
        List<TaskData> taskDataList = new ArrayList<>();

        List<DataDto> dataDesignations = taskDto.getDataDesignations();
        List<TypeData> typeDataList = taskDto.getTypeDataList();

        // Validate that dataDesignations and typeDataList have the same size
        if (dataDesignations.size() != typeDataList.size()) {
            throw new IllegalArgumentException("Mismatched sizes for dataDesignations and typeDataList");
        }

        // Iterate over dataDesignations and typeDataList to create TaskData entries
        for (int i = 0; i < dataDesignations.size(); i++) {
            DataDto dataDto = dataDesignations.get(i);
            TypeData typeData = typeDataList.get(i);

            // Check if the Data entity exists
            Data data = dataRepository.findByDesignation(dataDto.getDesignation())
                    .orElseGet(() -> {
                        // Create a new Data entity if not found
                        Data newData = new Data();
                        newData.setDesignation(dataDto.getDesignation());
                        dataService.generateCode(newData);// Generate a code for the new Data entity
                        return dataRepository.save(newData); // Save and return the new Data entity
                    });

            // Create and set TaskData properties
            TaskData taskData = new TaskData();
            taskData.setTask(task); // Ensure Task is set here
            taskData.setData(data);
            taskData.setType(typeData);

            // Add to list
            taskDataList.add(taskData);
        }

        // Set the list of TaskData for the Task
        task.setTaskDataList(taskDataList);

        // Save the Task entity first
        Task savedTask = taskRepository.save(task);

        // Update Task reference in TaskData and save TaskData entities
        for (TaskData taskData : taskDataList) {
            taskData.setTask(savedTask); // Ensure Task reference is updated
            taskDataRepository.save(taskData);
        }

        return savedTask;
    }

*/

    public Task addTask(TaskDto taskDto) {
        // Retrieve the associated Operation
        Operation operation = operationRepository.findById(taskDto.getOperation())
                .orElseThrow(() -> new IllegalArgumentException("Operation not found for id: " + taskDto.getOperation()));

        // Create and set up the Task
        Task task = new Task();
        task.setDesignation(taskDto.getDesignation());
        task.setOperation(operation);
        generateCode(task);
        return taskRepository.save(task);
    }


        public Task updateTask(UUID id, TaskDto taskDto) {
        Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

        Operation operation = operationRepository.findById(taskDto.getOperation())
                .orElseThrow(() -> new IllegalArgumentException("Operation not found for id: " + taskDto.getOperation()));

        taskToUpdate.setDesignation(taskDto.getDesignation());
        taskToUpdate.setOperation(operation);
        taskToUpdate.generateCode();

        return taskRepository.save(taskToUpdate);
    }

    public void generateCode(Task task) {

        String baseCode ="T-";

        Task lasttask = taskRepository.findTopByCodeStartingWithOrderByCodeDesc(baseCode);
        String lastCode = lasttask != null ? lasttask.getCode() : baseCode + "0000";

        // Extract the last number from the code
        String lastCodeNumberStr = lastCode.substring(baseCode.length());
        int lastCodeNumber = Integer.parseInt(lastCodeNumberStr);
        int newCodeNumber = lastCodeNumber + 1;

        // Generate the new code
        String newCode = baseCode + String.format("%04d", newCodeNumber);
        task.setCode(newCode);
    }

    public Task getTask(UUID id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        return taskOptional.orElse(null);
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByOperationId(UUID operationId) {
        return taskRepository.findByOperationId(operationId);
    }

    public void deleteTask(UUID id) {
        boolean exists = taskRepository.existsById(id);

        if (exists) {
            taskRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Task with id " + id + " does not exist");
        }
    }
}