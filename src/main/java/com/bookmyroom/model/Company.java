package com.bookmyroom.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.util.List;

@Data
@Document(collection = "companies")
public class Company {
    @Id
    private String id;
    
    @NotBlank(message = "Company name is required")
    @Indexed(unique = true)
    private String name;
    
    private String description;
    
    @NotBlank(message = "Company address is required")
    private String address;
    
    @Email(message = "Invalid email format")
    private String contactEmail;
    
    private String contactPhone;
    
    private List<String> officeIds;
    private List<String> userIds;
} 