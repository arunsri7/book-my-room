package com.bookmyroom.service;

import com.bookmyroom.model.Company;
import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();
    Company getCompanyById(String id);
    Company createCompany(Company company);
    Company updateCompany(String id, Company company);
    void deleteCompany(String id);
} 