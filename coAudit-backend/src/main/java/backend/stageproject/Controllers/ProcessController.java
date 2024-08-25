package backend.stageproject.Controllers;

import backend.stageproject.Dto.Pagedto;
import backend.stageproject.Dto.ProcessDto;
import backend.stageproject.Entity.Process;
import backend.stageproject.Repository.TaskDataRepository;
import backend.stageproject.Services.PDFExportService;
import backend.stageproject.Services.ProcessService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/process")
@AllArgsConstructor

public class ProcessController {

    private final ProcessService processService;
    private final PDFExportService pdfExportService;
    private TaskDataRepository taskDataRepository;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Process> addProcess(@RequestBody @NotNull ProcessDto processDto) {
        Process createdProcess = processService.addProcess(processDto);
        return ResponseEntity.ok(createdProcess);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Process> updateProcess(@PathVariable UUID id, @RequestBody @NotNull ProcessDto processDto) {
        Process updatedProcess = processService.updateProcess(id, processDto);
        return ResponseEntity.ok(updatedProcess);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("")
    public ResponseEntity<Pagedto<Process>> getProcess(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy) {

        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 10;
        if (StringUtils.isEmpty(sortBy)) sortBy = "id";

        Page<Process> processPage = processService.getProcessPage(PageRequest.of(offset, pageSize, Sort.by(sortBy)));
        Pagedto<Process> pagedto = new Pagedto<>(processPage.getContent(), processPage.getTotalElements());

        return ResponseEntity.ok(pagedto);
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SIMPLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Process> getProcessById(@PathVariable UUID id) {
        Process process = processService.getProcess(id);
        if (process != null) {
            return ResponseEntity.ok(process);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable UUID id) {
        boolean isDeleted = processService.deleteProcess(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/export/pdf/{id}")
    public void exportProcessesToPDF(@PathVariable UUID id, HttpServletResponse response) throws DocumentException, IOException {
        // Fetch the process by ID
        Process process = getProcessById(id).getBody();


            // Set response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=process_report.pdf");

        // Generate the PDF using Thymeleaf template
        pdfExportService.exportProcessToPDF(process, response);
    }



}
