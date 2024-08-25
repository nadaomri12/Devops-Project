package backend.stageproject.PDFGenerator;

import backend.stageproject.Entity.User;
import com.itextpdf.io.font.constants.StandardFonts;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserPDFExporter{

    private final TemplateEngine templateEngine;
    private final List<User> listUsers;

    public UserPDFExporter(TemplateEngine templateEngine, List<User> listUsers) {
        this.templateEngine = templateEngine;
        this.listUsers = listUsers;
    }

    public void export(HttpServletResponse response) throws IOException {
        Context context = new Context();
        context.setVariable("listUsers", listUsers);
        context.setVariable("generationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        String htmlContent = templateEngine.process("users-template", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=users.pdf");

        try (PdfWriter writer = new PdfWriter(response.getOutputStream());
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {


            // Parse HTML content to PDF
            ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
            com.itextpdf.html2pdf.HtmlConverter.convertToPdf(inputStream, writer);
        }
    }
}
