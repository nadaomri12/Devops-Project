package backend.stageproject.Controllers;

import backend.stageproject.Dto.TaskDto;
import backend.stageproject.Entity.Task;
import backend.stageproject.Entity.TaskData;
import backend.stageproject.Services.DataService;
import backend.stageproject.Services.TaskService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "/api")

@AllArgsConstructor

public class TaskController {
    @Autowired
    private final TaskService taskService;
    @Autowired
    private final DataService dataService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/task")
    public ResponseEntity<Task> addTask(@RequestBody @NotNull TaskDto taskDto) {
        Task taskToSave = taskService.addTask(taskDto);
        return ResponseEntity.ok(taskToSave);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/task/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @RequestBody @NotNull TaskDto taskDto) {
        Task updatedTask = taskService.updateTask(id, taskDto);
        return ResponseEntity.ok(updatedTask);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/task")
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskService.getTasks();
        if (!tasks.isEmpty()) {
            return ResponseEntity.ok(tasks);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/taskbyoperation/{operationId}")
    public ResponseEntity<List<Task>> getTasksByOperationId(@PathVariable UUID operationId) {
        List<Task> tasks = taskService.getTasksByOperationId(operationId);
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        Task task = taskService.getTask(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/task/{id}/data")
    public ResponseEntity<List<TaskData>> getTaskData(@PathVariable UUID id) {
        Task task = taskService.getTask(id);
        if (task != null) {
            return ResponseEntity.ok(dataService.getAllTaskData(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}