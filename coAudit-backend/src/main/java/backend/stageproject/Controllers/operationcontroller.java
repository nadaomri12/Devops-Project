package backend.stageproject.Controllers;
import backend.stageproject.Dto.operationDto;
import backend.stageproject.Entity.Operation;
import backend.stageproject.Services.ProcessService;
import backend.stageproject.Services.OperationService;
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

public class operationcontroller {
    @Autowired
    private final OperationService operationservice;
    private final ProcessService processService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/operation")
    public ResponseEntity<Operation> addOperation(@RequestBody @NotNull operationDto operationdto) {

       Operation optosave =operationservice.addOperation(operationdto);
        return ResponseEntity.ok(optosave);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/operation/{id}")
    public  ResponseEntity<Operation>  updateOperation(@PathVariable UUID id, @RequestBody @NotNull operationDto operationDto) {

        Operation updatedop = operationservice.updateoperation(id,operationDto);
        return ResponseEntity.ok(updatedop);

    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/operation")
    public ResponseEntity<List<Operation>> getOpeartion() {
        List<Operation> operation = operationservice.getOperation();
        if (!operation.isEmpty()) {
            return ResponseEntity.ok(operation); // 200 OK with the list of Objectives
        } else {
            return ResponseEntity.noContent().build(); // 204 No Content if no Objectives are found
        }
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/operationbyprocess/{processId}")
    public ResponseEntity<List<Operation>> getOperationByProcessId(@PathVariable UUID processId) {
        List<Operation> operationList = operationservice.getopeartionsByProcessId(processId);
        if (operationList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(operationList, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/operation/{id}")
    public ResponseEntity<Operation> getoperationById(@PathVariable UUID id) {
        Operation operation = operationservice.getOperation(id);
        if (operation != null) {
            return ResponseEntity.ok(operation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/operation/{id}")
    public ResponseEntity<Void> deleteoperation(@PathVariable UUID id) {
        operationservice.deleteOperation(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
    }



}
