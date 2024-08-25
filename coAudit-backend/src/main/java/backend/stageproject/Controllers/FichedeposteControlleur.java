package backend.stageproject.Controllers;
/*
import backend.stageproject.Services.PDFExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class FichedeposteControlleur {

    private final PDFExportService pdfExportService;

    @GetMapping("/exportfichedeposte/{poste}")
    public void exportFicheDePoste(
            @PathVariable String poste,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=fichedeposte.pdf");
        pdfExportService.exportFichedeposteToPDF(poste, response);
    }
}
*/