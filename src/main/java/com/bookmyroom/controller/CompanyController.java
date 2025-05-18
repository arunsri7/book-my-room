package com.bookmyroom.controller;

import com.bookmyroom.model.Company;
import com.bookmyroom.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Company>> getAllCompanies() {
        log.debug("Received request to get all companies");
        List<Company> companies = companyService.getAllCompanies();
        log.debug("Returning {} companies", companies.size());
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) {
        log.debug("Received request to get company with id: {}", id);
        Company company = companyService.getCompanyById(id);
        log.debug("Found company: {}", company);
        return ResponseEntity.ok(company);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        log.info("Received request to create company: {}", company.getName());
        Company createdCompany = companyService.createCompany(company);
        log.info("Successfully created company with id: {}", createdCompany.getId());
        return ResponseEntity.ok(createdCompany);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Company> updateCompany(@PathVariable String id, @Valid @RequestBody Company company) {
        log.info("Received request to update company with id: {}", id);
        Company updatedCompany = companyService.updateCompany(id, company);
        log.info("Successfully updated company with id: {}", updatedCompany.getId());
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteCompany(@PathVariable String id) {
        log.info("Received request to delete company with id: {}", id);
        companyService.deleteCompany(id);
        log.info("Successfully deleted company with id: {}", id);
        return ResponseEntity.ok().build();
    }
} 