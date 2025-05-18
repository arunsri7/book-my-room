package com.bookmyroom.service.impl;

import com.bookmyroom.exception.ResourceNotFoundException;
import com.bookmyroom.exception.DuplicateEntityException;
import com.bookmyroom.model.Company;
import com.bookmyroom.repository.CompanyRepository;
import com.bookmyroom.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public List<Company> getAllCompanies() {
        log.debug("Fetching all companies from repository");
        List<Company> companies = companyRepository.findAll();
        log.debug("Retrieved {} companies from repository", companies.size());
        return companies;
    }

    @Override
    public Company getCompanyById(String id) {
        log.debug("Fetching company with id: {} from repository", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Company not found with id: {}", id);
                    return new ResourceNotFoundException("Company not found with id: " + id);
                });
        log.debug("Retrieved company: {}", company);
        return company;
    }

    @Override
    public Company createCompany(Company company) {
        log.info("Starting company creation process for company: {}", company.getName());
        
        // Check for duplicate company name
        log.debug("Checking for duplicate company name: {}", company.getName());
        if (companyRepository.existsByName(company.getName())) {
            throw new DuplicateEntityException("Company", "name " + company.getName());
        }
        log.debug("No duplicate company found");

        Company savedCompany = companyRepository.save(company);
        log.info("Successfully created company with id: {}", savedCompany.getId());
        return savedCompany;
    }

    @Override
    public Company updateCompany(String id, Company company) {
        log.info("Starting company update process for company id: {}", id);
        Company existingCompany = getCompanyById(id);
        
        if (!existingCompany.getName().equals(company.getName())) {
            log.debug("Checking for duplicate company name: {}", company.getName());
            if (companyRepository.existsByName(company.getName())) {
                throw new DuplicateEntityException("Company", "name " + company.getName());
            }
            log.debug("No duplicate company found");
        }

        existingCompany.setName(company.getName());
        existingCompany.setDescription(company.getDescription());
        existingCompany.setAddress(company.getAddress());
        existingCompany.setContactEmail(company.getContactEmail());
        existingCompany.setContactPhone(company.getContactPhone());
        log.debug("Updated company details for company id: {}", id);

        Company updatedCompany = companyRepository.save(existingCompany);
        log.info("Successfully updated company with id: {}", updatedCompany.getId());
        return updatedCompany;
    }

    @Override
    public void deleteCompany(String id) {
        log.info("Starting company deletion process for company id: {}", id);
        Company company = getCompanyById(id);
        companyRepository.delete(company);
        log.info("Successfully deleted company with id: {}", id);
    }
} 