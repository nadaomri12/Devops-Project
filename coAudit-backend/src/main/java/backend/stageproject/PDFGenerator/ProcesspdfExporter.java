package backend.stageproject.PDFGenerator;

import backend.stageproject.Entity.*;
import backend.stageproject.Entity.Process;
import backend.stageproject.Repository.TaskDataRepository;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;

import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ProcesspdfExporter {

    private final Process process;
    private final TemplateEngine templateEngine;

    @Autowired
    private TaskDataRepository taskDataRepository;
    public ProcesspdfExporter(TemplateEngine templateEngine, Process process) {
        this.process = process;
        this.templateEngine = templateEngine;

    }

    public void export(HttpServletResponse response) throws IOException {
        // Prepare Thymeleaf context
        Context context = new Context();
        String generationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        context.setVariable("generationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        context.setVariable("process", process);
        List<TaskData> inputData = new ArrayList<>();
        List<TaskData> outputData = new ArrayList<>();

        // Iterate through operations and tasks to collect input and output taskdata
        for (Operation operation : process.getOperationList()) {

            for (Task task : operation.getTasks()) {
                // Fetch input data for the task
                List<TaskData> taskInputData = taskDataRepository.findAllByTaskOperationProcessIdAndType(
                         task.getId(), TypeData.INPUT);

                inputData.addAll(taskInputData);

                // Fetch output data for the task
                List<TaskData> taskOutputData = taskDataRepository.findAllByTaskOperationProcessIdAndType(
                       task.getId(), TypeData.OUTPUT);
                outputData.addAll(taskOutputData);
            }
        }
      System.out.println(inputData);
        System.out.println(outputData);
        // Set variables in the context after collecting all data
        context.setVariable("inputData", inputData);
        context.setVariable("outputData", outputData);

        // Render template to HTML
        String htmlContent = templateEngine.process("process-template", context);

        // Set response properties
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=process-report.pdf");

        // Convert HTML to PDF
        try (com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(response.getOutputStream());
             com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new PdfDocument(writer);
             com.itextpdf.layout.Document document = new Document(pdfDoc)) {

            // Convert HTML to PDF
            ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
            HtmlConverter.convertToPdf(inputStream, pdfDoc);
        }

    }}
