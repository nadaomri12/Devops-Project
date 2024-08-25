package backend.stageproject.Controllers;

import backend.stageproject.Dto.DataDto;
import backend.stageproject.Entity.Data;
import backend.stageproject.Entity.Objective;
import backend.stageproject.Services.DataService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor

public class DataController {
    @Autowired
    private final DataService dataService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/data")
    public ResponseEntity<Data> addData(@RequestBody @NotNull DataDto dataDto) {
        dataService.addData(dataDto);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/data/taskData/{id}")
    public ResponseEntity<Data> updateData(@PathVariable UUID id, @RequestBody @NotNull DataDto dataDto) {
        dataService.updateData(id, dataDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/data")
    public ResponseEntity<List<Data>> getAllData() {
        List<Data> dataList = dataService.getAllData();
        if (!dataList.isEmpty()) {
            return ResponseEntity.ok(dataList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/databytask/{taskId}")
    public ResponseEntity<List<Data>> getDATABytaskId(@PathVariable UUID taskId) {
        List<Data> data = dataService.getDataBytaskId(taskId);
        if (data.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/data/{id}")
    public ResponseEntity<Data> getDataById(@PathVariable UUID id) {
        Data data = dataService.getData(id);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/data/{id}")
    public ResponseEntity<Void> deleteData(@PathVariable UUID id) {
        dataService.deleteData(id);
        return ResponseEntity.noContent().build();
    }
}