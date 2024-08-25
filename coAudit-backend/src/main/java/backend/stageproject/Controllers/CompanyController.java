package backend.stageproject.Controllers;

import backend.stageproject.Dto.CompanyDto;
import backend.stageproject.Entity.Company;
import backend.stageproject.Entity.Objective;
import backend.stageproject.Entity.User;
import backend.stageproject.Repository.CompanyRepository;
import backend.stageproject.Repository.UserRepository;
import backend.stageproject.Services.CompanyService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")

public class CompanyController {
    @Autowired
    private final CompanyService companyService;
    @PostMapping(value = "/company", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Company> addCompany(
            @RequestPart  CompanyDto companyDto,
            @RequestPart(value = "file") MultipartFile file) {
        System.out.println(file.getContentType());
        Company company = companyService.addCompany(companyDto, file);
        return ResponseEntity.ok(company);
    }



    @PutMapping("/company/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable UUID id, @RequestParam("company") CompanyDto companyDto, @RequestParam(value = "file", required = false) MultipartFile file) {
        Company company = companyService.updateCompany(id, companyDto, file);
        return ResponseEntity.ok(company);
    }
    @GetMapping("/company")
    public List<Company> getCompany() {

        return companyService.getCompany();
    }

    @GetMapping("/company/{id}")
    public ResponseEntity <Company>  getCompany(@PathVariable UUID id) {
        Company company =companyService.getCompany(id);
        if (company != null) {
            return ResponseEntity.ok(company); // Return 200 OK with the requested Objective entity
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if Objective with id not found
        }
    }


}


