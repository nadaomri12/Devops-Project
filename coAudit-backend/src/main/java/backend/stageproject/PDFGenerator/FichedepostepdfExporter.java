package backend.stageproject.PDFGenerator;

import backend.stageproject.Entity.Objective;
import backend.stageproject.Entity.Operation;
import backend.stageproject.Entity.Poste;
import backend.stageproject.Repository.ObjectiveRepository;
import backend.stageproject.Repository.OperationRepository;
import backend.stageproject.Repository.PosteRepository;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.thymeleaf.context.Context;
@Component
@AllArgsConstructor
public class FichedepostepdfExporter {
    private final TemplateEngine templateEngine;
    private final OperationRepository operationRepository;
    private final PosteRepository posteRepository;
    private final ObjectiveRepository objectiveRepository;

    public void export(HttpServletResponse response, UUID poste) {
        try {
            Context context = new Context();
            Poste postee = posteRepository.findById(poste)
                    .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + poste));

            List<Operation> operations = operationRepository.findByResponsibility(postee);
            List<Objective> objectives=objectiveRepository.findAll();
            context.setVariable("objectives", objectives);
            context.setVariable("postee", postee);  // Use "postee" instead of "poste"
            context.setVariable("operations", operations);
            context.setVariable("generationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            context.setVariable("operations", operations);
            context.setVariable("generationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            String htmlContent = templateEngine.process("Fichedeposte-template", context);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=fichedeposte.pdf");
            response.setCharacterEncoding("UTF-8");

            try (PdfWriter writer = new PdfWriter(response.getOutputStream());
                 PdfDocument pdfDoc = new PdfDocument(writer);
                 Document document = new Document(pdfDoc)) {

                // Convert HTML content to PDF
                ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
                HtmlConverter.convertToPdf(inputStream, writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Error generating PDF: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
