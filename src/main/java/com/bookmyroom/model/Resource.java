package com.bookmyroom.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Document(collection = "resources")
@CompoundIndex(def = "{'name': 1, 'officeId': 1}", unique = true)
public class Resource {
    @Id
    private String id;
    
    @NotBlank(message = "Resource name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Resource type is required")
    private ResourceType type;
    
    @NotBlank(message = "Office ID is required")
    private String officeId;
    
    @NotBlank(message = "Company ID is required")
    private String companyId;
    
    private int capacity;
    private boolean isAvailable;

    public enum ResourceType {
        CONFERENCE_ROOM,
        HOT_DESK,
        PROJECTOR,
        OTHER
    }
} 