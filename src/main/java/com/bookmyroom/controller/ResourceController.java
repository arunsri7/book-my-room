package com.bookmyroom.controller;

import com.bookmyroom.model.Resource;
import com.bookmyroom.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<List<Resource>> getAllResources() {
        log.debug("Received request to get all resources");
        List<Resource> resources = resourceService.getAllResources();
        log.debug("Returning {} resources", resources.size());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<Resource> getResourceById(@PathVariable String id) {
        log.debug("Received request to get resource with id: {}", id);
        Resource resource = resourceService.getResourceById(id);
        log.debug("Found resource: {}", resource);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<Resource> createResource(@Valid @RequestBody Resource resource) {
        log.info("Received request to create resource: {}", resource.getName());
        Resource createdResource = resourceService.createResource(resource);
        log.info("Successfully created resource with id: {}", createdResource.getId());
        return ResponseEntity.ok(createdResource);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<Resource> updateResource(@PathVariable String id, @Valid @RequestBody Resource resource) {
        log.info("Received request to update resource with id: {}", id);
        Resource updatedResource = resourceService.updateResource(id, resource);
        log.info("Successfully updated resource with id: {}", updatedResource.getId());
        return ResponseEntity.ok(updatedResource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<Void> deleteResource(@PathVariable String id) {
        log.info("Received request to delete resource with id: {}", id);
        resourceService.deleteResource(id);
        log.info("Successfully deleted resource with id: {}", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<List<Resource>> getResourcesByCompany(
            @PathVariable String companyId,
            @RequestParam(required = false) String officeId,
            @RequestParam(required = false) Resource.ResourceType type) {
        log.debug("Received request to get resources for company: {} with filters - office: {}, type: {}", 
                companyId, officeId, type);
        
        List<Resource> resources;
        if (officeId != null && type != null) {
            resources = resourceService.getResourcesByCompanyAndOfficeAndType(companyId, officeId, type);
        } else if (officeId != null) {
            resources = resourceService.getResourcesByCompanyAndOffice(companyId, officeId);
        } else if (type != null) {
            resources = resourceService.getResourcesByCompanyAndType(companyId, type);
        } else {
            resources = resourceService.getResourcesByCompany(companyId);
        }
        
        log.debug("Found {} resources for company {} with applied filters", resources.size(), companyId);
        return ResponseEntity.ok(resources);
    }
} 