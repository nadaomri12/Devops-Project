package backend.stageproject.Controllers;

import backend.stageproject.Auth.RegisterRequestDto;
import backend.stageproject.Dto.Pagedto;
import backend.stageproject.Entity.User;
import backend.stageproject.PDFGenerator.UserPDFExporter;
import backend.stageproject.Repository.UserRepository;

import backend.stageproject.Services.PDFExportService;
import backend.stageproject.Services.UserService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")

public class UserController {
    private final UserRepository userRepository;
    private final UserService userservice;
    private final PasswordEncoder passwordEncoder;
    private final PDFExportService pdfExportService;

    @GetMapping("/users")
    public ResponseEntity<Pagedto<User>> getUsers(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy) {

        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 10;
        if (StringUtils.isEmpty(sortBy)) sortBy = "id";

        // Use 'createdAt' for sorting, or any other field as needed
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));

        Page<User> usersPage = userservice.getUsersPage(PageRequest.of(offset, pageSize, sort));
        Pagedto<User> pagedto = new Pagedto<>(usersPage.getContent(), usersPage.getTotalElements());

        return ResponseEntity.ok(pagedto);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUsers(@PathVariable UUID id) {
        User user = userservice.getUser(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody @NotNull RegisterRequestDto user) {
        User usertoupdate = userservice.updateUser(id, user);
        return ResponseEntity.ok(usertoupdate);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
        userservice.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /* @GetMapping("/users/export/pdf")
     public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
         response.setContentType("application/pdf");
         DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
         String currentDateTime = dateFormatter.format(new Date());

         String headerKey = "Content-Disposition";
         String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
         response.setHeader(headerKey, headerValue);

         // Define a PageRequest with a suitable page size, e.g., 1000
         Sort sort = Sort.by(Sort.Order.desc("createdAt"));
         PageRequest pageRequest = PageRequest.of(0, 1000, sort);
         Page<User> userPage = userservice.getUsersPage(pageRequest);
         List<User> listUsers = userPage.getContent();

         UserPDFExporter exporter = new UserPDFExporter(listUsers);
         exporter.export(response);
     }*/
    @GetMapping("/users/export/pdf")
    public void exportPdf(HttpServletResponse response) {
        try {
            // Configurer la pagination et le tri
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageRequest = PageRequest.of(0, 1000, sort);

            // Récupérer les utilisateurs
            Page<User> userPage = userservice.getUsersPage(pageRequest);
            List<User> listUsers = userPage.getContent();

            // Configurer la réponse HTTP pour le PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=users.pdf");

            // Exporter les utilisateurs en PDF
            pdfExportService.exportUsersToPDF(listUsers, response);
        } catch (DocumentException e) {
            // Gestion spécifique des exceptions liées au document PDF
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Erreur lors de la génération du PDF : " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Log the exception or handle it accordingly
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
