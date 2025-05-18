package com.bookmyroom.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "offices")
public class Office {
    @Id
    private String id;
    private String name;
    private String address;
    private String companyId;
    private List<String> resourceIds;
} 