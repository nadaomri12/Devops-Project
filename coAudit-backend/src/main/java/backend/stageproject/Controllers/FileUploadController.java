package backend.stageproject.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class FileUploadController {
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Veuillez sélectionner un fichier à télécharger.");
        }

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            return ResponseEntity.status(HttpStatus.OK).body("Fichier téléchargé avec succès : " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du téléchargement du fichier : " + e.getMessage());
        }
    }
}
