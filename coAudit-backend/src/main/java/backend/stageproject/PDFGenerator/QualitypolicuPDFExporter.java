package backend.stageproject.PDFGenerator;

import backend.stageproject.Entity.QualityPolicy;
import backend.stageproject.Entity.User;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class QualitypolicuPDFExporter {
    private final TemplateEngine templateEngine;
    private List<QualityPolicy> qualityPolicyList;


    public QualitypolicuPDFExporter(TemplateEngine templateEngine, List<QualityPolicy> qualityPolicyList) {
        this.templateEngine = templateEngine;
        this.qualityPolicyList = qualityPolicyList;
    }

    public void export(HttpServletResponse response) throws IOException {
        Context context = new Context();
        context.setVariable("qualityPolicyList", qualityPolicyList);
        context.setVariable("generationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        String htmlContent = templateEngine.process("qualitypolicy-template", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=qualitypolicy.pdf");

        try (com.itextpdf.kernel.pdf.PdfWriter writer = new PdfWriter(response.getOutputStream());
             PdfDocument pdfDoc = new PdfDocument(writer);
             com.itextpdf.layout.Document document = new Document(pdfDoc)) {


            // Parse HTML content to PDF
            ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
            com.itextpdf.html2pdf.HtmlConverter.convertToPdf(inputStream, writer);
        }
    }

}


