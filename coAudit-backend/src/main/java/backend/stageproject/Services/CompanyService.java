package backend.stageproject.Services;

import backend.stageproject.Dto.CompanyDto;

import backend.stageproject.Entity.Company;

import backend.stageproject.Entity.User;
import backend.stageproject.Repository.CompanyRepository;
import backend.stageproject.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;




    @Transactional
    public Company addCompany(CompanyDto companyDto, MultipartFile file) {
        // Find responsible user by email
        User responsible = userRepository.findByEmail(companyDto.getResponsible())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + companyDto.getResponsible()));

        // Create a new Company entity and set its properties
        Company company = new Company();
        company.setAddress(companyDto.getAddress());
        company.setName(companyDto.getName());
        company.setPhone(companyDto.getPhone());
        company.setResponsible(responsible);

        if (file != null && !file.isEmpty()) {
            try {
                // Assuming you are storing image data as bytes
                company.setImage(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file data", e);
            }
        }
        // Generate the company code
        company.generateCode();

        // Save the company entity to the database
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateCompany(UUID id, CompanyDto companyDto, MultipartFile file) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));

        User responsible = userRepository.findByEmail(companyDto.getResponsible())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + companyDto.getResponsible()));

        company.setAddress(companyDto.getAddress());
        company.setName(companyDto.getName());
        company.setPhone(companyDto.getPhone());
        company.setResponsible(responsible);

        if (file != null && !file.isEmpty()) {
            try {
                company.setImage(file.getBytes()); // Assuming image data is stored as bytes
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file data", e);
            }
        }

        company.generateCode();
        return companyRepository.save(company);
    }

    public List<Company> getCompany() {

        return companyRepository.findAll();
    }

    public Company getCompany(UUID id) {
        Optional<Company> userOptional = companyRepository.findById(id);

        return userOptional.orElse(null);    }
}
