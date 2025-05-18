package com.bookmyroom.service;

import com.bookmyroom.model.Resource;
import java.util.List;

public interface ResourceService {
    List<Resource> getAllResources();
    Resource getResourceById(String id);
    Resource createResource(Resource resource);
    Resource updateResource(String id, Resource resource);
    void deleteResource(String id);
    List<Resource> getResourcesByCompany(String companyId);
    List<Resource> getResourcesByCompanyAndOffice(String companyId, String officeId);
    List<Resource> getResourcesByCompanyAndType(String companyId, Resource.ResourceType type);
    List<Resource> getResourcesByCompanyAndOfficeAndType(String companyId, String officeId, Resource.ResourceType type);
    boolean isResourceInCompany(String resourceId, String companyId);
} 