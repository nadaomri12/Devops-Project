package backend.stageproject.Controllers;

import backend.stageproject.Services.EmailService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/email")

public class Emailcontroller {
    private EmailService emailService;

    @Autowired
    public Emailcontroller(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendMail(@RequestParam(value = "file", required = false) MultipartFile[] file, String[] to, String subject, String body) {
        return emailService.sendMail(file, to, subject, body);
    }
}
