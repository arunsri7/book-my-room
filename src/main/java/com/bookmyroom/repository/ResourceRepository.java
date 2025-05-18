package com.bookmyroom.repository;

import com.bookmyroom.model.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends MongoRepository<Resource, String> {
    List<Resource> findByOfficeId(String officeId);
    List<Resource> findByType(Resource.ResourceType type);
    List<Resource> findByCompanyId(String companyId);
    List<Resource> findByCompanyIdAndOfficeId(String companyId, String officeId);
    List<Resource> findByCompanyIdAndType(String companyId, Resource.ResourceType type);
    List<Resource> findByCompanyIdAndOfficeIdAndType(String companyId, String officeId, Resource.ResourceType type);
    List<Resource> findByOfficeIdAndCompanyId(String officeId, String companyId);
    List<Resource> findByTypeAndCompanyId(Resource.ResourceType type, String companyId);
} 