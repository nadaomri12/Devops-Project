package backend.stageproject.Services;

import backend.stageproject.Entity.*;
import backend.stageproject.Entity.Process;
//import backend.stageproject.PDFGenerator.FichedepostepdfExporter;
import backend.stageproject.PDFGenerator.FichedepostepdfExporter;
import backend.stageproject.PDFGenerator.QualitypolicuPDFExporter;
import backend.stageproject.PDFGenerator.UserPDFExporter;
import backend.stageproject.Repository.OperationRepository;
import backend.stageproject.Repository.TaskDataRepository;
import com.itextpdf.html2pdf.HtmlConverter;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PDFExportService {

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private TaskDataRepository taskDataRepository;
    @Autowired
    private final FichedepostepdfExporter fichedepostepdfExporter;
    @Autowired
    private OperationRepository operationRepository;
    public void exportUsersToPDF(List<User> users, HttpServletResponse response) throws IOException, DocumentException {


        // Créer l'exportateur PDF avec le contenu HTML
        UserPDFExporter exporter = new UserPDFExporter(templateEngine, users);
        exporter.export(response);
    }


    public void exportQPToPDF(List<QualityPolicy> qualityPolicyList, HttpServletResponse response) throws IOException {

        QualitypolicuPDFExporter exporter = new QualitypolicuPDFExporter(templateEngine, qualityPolicyList);
        exporter.export(response);
    }



    public void exportProcessToPDF(Process process, HttpServletResponse response) throws IOException {
        // Set up Thymeleaf context with the process object
        Context context = new Context();
        context.setVariable("process", process);
        context.setVariable("generationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        List<TaskData> inputData = new ArrayList<>();
        List<TaskData> outputData = new ArrayList<>();


        // Iterate through operations and tasks to collect input and output data
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

        // Set variables in the context after collecting all data
        context.setVariable("inputData", inputData);
        context.setVariable("outputData", outputData);

        // Generate HTML content using Thymeleaf template
        String htmlContent = templateEngine.process("process-template", context);

        // Set response properties for PDF download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=process_report.pdf");

        // Convert HTML to PDF and write to response output stream using iText
        try (OutputStream outputStream = response.getOutputStream()) {
            HtmlConverter.convertToPdf(htmlContent, outputStream);

        }
    }


        public void exportFichedeposteToPDF(UUID poste, HttpServletResponse response) throws IOException {
            fichedepostepdfExporter.export(response, poste);
        }


}
