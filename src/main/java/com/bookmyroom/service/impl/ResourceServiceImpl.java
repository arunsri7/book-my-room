package com.bookmyroom.service.impl;

import com.bookmyroom.exception.ResourceNotFoundException;
import com.bookmyroom.exception.UnauthorizedException;
import com.bookmyroom.model.Resource;
import com.bookmyroom.model.User;
import com.bookmyroom.model.UserRole;
import com.bookmyroom.repository.ResourceRepository;
import com.bookmyroom.security.CustomUserDetails;
import com.bookmyroom.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    private User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

    @Override
    public List<Resource> getAllResources() {
        User currentUser = getCurrentUser();
        
        if (currentUser.getRole().equals(UserRole.SUPER_ADMIN)) {
            return resourceRepository.findAll();
        } else if (currentUser.getRole().equals(UserRole.COMPANY_ADMIN) || 
                  currentUser.getRole().equals(UserRole.USER)) {
            return resourceRepository.findByCompanyId(currentUser.getCompanyId());
        }
        
        throw new UnauthorizedException("User does not have permission to access resources");
    }

    @Override
    public Resource getResourceById(String id) {
        return resourceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
    }

    @Override
    public Resource createResource(Resource resource) {
        User currentUser = getCurrentUser();
        
        if (!currentUser.getRole().equals(UserRole.SUPER_ADMIN) && 
            !currentUser.getRole().equals(UserRole.COMPANY_ADMIN)) {
            throw new UnauthorizedException("User does not have permission to create resources");
        }
        
        resource.setCompanyId(currentUser.getCompanyId());
        resource.setAvailable(true);
        return resourceRepository.save(resource);
    }

    @Override
    public Resource updateResource(String id, Resource resource) {
        User currentUser = getCurrentUser();
        
        if (!currentUser.getRole().equals(UserRole.SUPER_ADMIN) && 
            !currentUser.getRole().equals(UserRole.COMPANY_ADMIN)) {
            throw new UnauthorizedException("User does not have permission to update resources");
        }
        
        Resource existingResource = getResourceById(id);
        
        if (!currentUser.getRole().equals(UserRole.SUPER_ADMIN) && 
            !existingResource.getCompanyId().equals(currentUser.getCompanyId())) {
            throw new UnauthorizedException("User does not have permission to update this resource");
        }
        
        resource.setId(id);
        resource.setCompanyId(existingResource.getCompanyId());
        return resourceRepository.save(resource);
    }

    @Override
    public void deleteResource(String id) {
        User currentUser = getCurrentUser();
        
        if (!currentUser.getRole().equals(UserRole.SUPER_ADMIN) && 
            !currentUser.getRole().equals(UserRole.COMPANY_ADMIN)) {
            throw new UnauthorizedException("User does not have permission to delete resources");
        }
        
        Resource resource = getResourceById(id);
        
        if (!currentUser.getRole().equals(UserRole.SUPER_ADMIN) && 
            !resource.getCompanyId().equals(currentUser.getCompanyId())) {
            throw new UnauthorizedException("User does not have permission to delete this resource");
        }
        
        resourceRepository.deleteById(id);
    }

    @Override
    public List<Resource> getResourcesByCompany(String companyId) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getRole().equals(UserRole.SUPER_ADMIN)) {
            return resourceRepository.findByCompanyId(companyId);
        } else if (currentUser.getRole().equals(UserRole.COMPANY_ADMIN) || 
                  currentUser.getRole().equals(UserRole.USER)) {
            if (!companyId.equals(currentUser.getCompanyId())) {
                throw new UnauthorizedException("User does not have permission to access resources from other companies");
            }
            return resourceRepository.findByCompanyId(companyId);
        }
        
        throw new UnauthorizedException("User does not have permission to access resources");
    }

    @Override
    public List<Resource> getResourcesByCompanyAndOffice(String companyId, String officeId) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getRole().equals(UserRole.SUPER_ADMIN)) {
            return resourceRepository.findByCompanyIdAndOfficeId(companyId, officeId);
        } else if (currentUser.getRole().equals(UserRole.COMPANY_ADMIN) || 
                  currentUser.getRole().equals(UserRole.USER)) {
            if (!companyId.equals(currentUser.getCompanyId())) {
                throw new UnauthorizedException("User does not have permission to access resources from other companies");
            }
            return resourceRepository.findByCompanyIdAndOfficeId(companyId, officeId);
        }
        
        throw new UnauthorizedException("User does not have permission to access resources");
    }

    @Override
    public List<Resource> getResourcesByCompanyAndType(String companyId, Resource.ResourceType type) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getRole().equals(UserRole.SUPER_ADMIN)) {
            return resourceRepository.findByCompanyIdAndType(companyId, type);
        } else if (currentUser.getRole().equals(UserRole.COMPANY_ADMIN) || 
                  currentUser.getRole().equals(UserRole.USER)) {
            if (!companyId.equals(currentUser.getCompanyId())) {
                throw new UnauthorizedException("User does not have permission to access resources from other companies");
            }
            return resourceRepository.findByCompanyIdAndType(companyId, type);
        }
        
        throw new UnauthorizedException("User does not have permission to access resources");
    }

    @Override
    public List<Resource> getResourcesByCompanyAndOfficeAndType(String companyId, String officeId, Resource.ResourceType type) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getRole().equals(UserRole.SUPER_ADMIN)) {
            return resourceRepository.findByCompanyIdAndOfficeIdAndType(companyId, officeId, type);
        } else if (currentUser.getRole().equals(UserRole.COMPANY_ADMIN) || 
                  currentUser.getRole().equals(UserRole.USER)) {
            if (!companyId.equals(currentUser.getCompanyId())) {
                throw new UnauthorizedException("User does not have permission to access resources from other companies");
            }
            return resourceRepository.findByCompanyIdAndOfficeIdAndType(companyId, officeId, type);
        }
        
        throw new UnauthorizedException("User does not have permission to access resources");
    }

    @Override
    public boolean isResourceInCompany(String resourceId, String companyId) {
        Resource resource = getResourceById(resourceId);
        return resource.getCompanyId().equals(companyId);
    }
} 