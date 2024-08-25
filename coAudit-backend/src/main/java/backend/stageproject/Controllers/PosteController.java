package backend.stageproject.Controllers;

import backend.stageproject.Dto.PosteDto;
import backend.stageproject.Dto.QuallityPolicyDto;
import backend.stageproject.Entity.Poste;
import backend.stageproject.Entity.QualityPolicy;
import backend.stageproject.Services.PDFExportService;
import backend.stageproject.Services.PosteService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/poste")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class PosteController {
    private PosteService posteService;
    @PreAuthorize("hasAuthority('ADMIN')")

    @PostMapping("")
    public ResponseEntity<Poste> addPoste(@RequestBody @NotNull PosteDto posteDto) {
        posteService.findOrCreatePoste(posteDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Poste> updatePoste(@PathVariable UUID id, @RequestBody @NotNull PosteDto posteDto) {
        Poste updatePoste =posteService.updatePoste(id, posteDto);
        return ResponseEntity.ok(updatePoste);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("")
    public ResponseEntity<List<Poste>> getAllPoste() {
        List<Poste> PostList = posteService.getAllPoste();
        if (!PostList.isEmpty()) {
            return ResponseEntity.ok(PostList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    private final PDFExportService pdfExportService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteData(@PathVariable UUID id) {
        posteService.deletePoste(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/exportfichedeposte/{posteid}")
    public void exportFicheDePoste(
            @PathVariable("posteid") UUID poste,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=fichedeposte.pdf");
        pdfExportService.exportFichedeposteToPDF(poste, response);
    }

}
