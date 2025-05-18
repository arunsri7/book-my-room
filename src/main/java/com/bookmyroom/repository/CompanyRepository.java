package com.bookmyroom.repository;

import com.bookmyroom.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
    boolean existsByName(String name);
} 