package backend.stageproject.Controllers;

import backend.stageproject.Dto.ObjectiveDto;
import backend.stageproject.Entity.*;
import backend.stageproject.Entity.Process;
import backend.stageproject.Services.ObjectiveService;
import backend.stageproject.Services.PDFExportService;
import backend.stageproject.Services.ProcessService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor

public class ObjectiveController {
    @Autowired
    private final ObjectiveService objectiveService;
 private final ProcessService processService;
    private final PDFExportService pdfExportService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/objective")
    public  ResponseEntity<Objective> addObjective(@RequestBody @NotNull ObjectiveDto objectiveDto) {

      Objective objtosave =objectiveService.addObj(objectiveDto);
        return ResponseEntity.ok(objtosave);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/objective/{id}")
    public  ResponseEntity<Objective >  updateObjective(@PathVariable UUID id, @RequestBody @NotNull ObjectiveDto objectiveDto) {

        Objective updatedObj = objectiveService.updateobj(id,objectiveDto);
        return ResponseEntity.ok(updatedObj);

    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/objective")
    public ResponseEntity<List<Objective>> getObjectives() {
        List<Objective> objectives = objectiveService.getObjective();
        if (!objectives.isEmpty()) {
            return ResponseEntity.ok(objectives); // 200 OK with the list of Objectives
        } else {
            return ResponseEntity.noContent().build(); // 204 No Content if no Objectives are found
        }
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/objectivebyprocess/{processId}")
    public ResponseEntity<List<Objective>> getObjectivesByProcessId(@PathVariable UUID processId) {
        List<Objective> objectives = objectiveService.getObjectivesByProcessId(processId);
        if (objectives.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(objectives, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/objective/{id}")
    public ResponseEntity<Objective> getObjectiveById(@PathVariable UUID id) {
        Objective objective = objectiveService.getObj(id);
        if (objective != null) {
            return ResponseEntity.ok(objective); // Return 200 OK with the requested Objective entity
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if Objective with id not found
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/objective/{id}")
    public ResponseEntity<Void> deleteObjective(@PathVariable UUID id) {
        objectiveService.deleteObjective(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
    }




}