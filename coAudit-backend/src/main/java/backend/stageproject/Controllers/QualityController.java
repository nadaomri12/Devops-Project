package backend.stageproject.Controllers;

import backend.stageproject.Dto.ProcessDto;
import backend.stageproject.Dto.QuallityPolicyDto;

import backend.stageproject.Entity.Process;
import backend.stageproject.Entity.QualityPolicy;

import backend.stageproject.Entity.User;
import backend.stageproject.PDFGenerator.QualitypolicuPDFExporter;
import backend.stageproject.PDFGenerator.UserPDFExporter;
import backend.stageproject.Services.PDFExportService;
import backend.stageproject.Services.QualityService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "/api/qualitypolicy")
@AllArgsConstructor


public class QualityController {

    private final QualityService qualityService;
    private final PDFExportService pdfExportService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public  ResponseEntity<QualityPolicy> addQP(@RequestBody @NotNull QuallityPolicyDto QP){

        QualityPolicy createdQP= qualityService.addQP(QP);
        return ResponseEntity.ok(createdQP);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<QualityPolicy> updateQP(@PathVariable UUID id, @RequestBody @NotNull QuallityPolicyDto QP) {
        QualityPolicy updatedQP =qualityService.updateQP(id, QP);
        return ResponseEntity.ok(updatedQP);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<QualityPolicy> getQP(@PathVariable UUID id) {
        QualityPolicy Pq = qualityService.getQP(id);;
        if (Pq != null) {
            return ResponseEntity.ok(Pq);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping
    public ResponseEntity<List<QualityPolicy>> getQP() {
        List<QualityPolicy> qualityPolicies = qualityService.getQP();
        if (!qualityPolicies.isEmpty()) {
            return ResponseEntity.ok(qualityPolicies); // 200 OK with the list of QualityPolicy entities
        } else {
            return ResponseEntity.noContent().build(); // 204 No Content if no QualityPolicy entities are found
        }
    }




    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQualityPolicy(@PathVariable UUID id) {
        qualityService.deleteQP(id);
        return ResponseEntity.noContent().build(); // 204 No Content

    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {


        List<QualityPolicy> qualityPolicyList = qualityService.getQP();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=qualitypolicy.pdf");

        pdfExportService.exportQPToPDF(qualityPolicyList, response);

    }

}


