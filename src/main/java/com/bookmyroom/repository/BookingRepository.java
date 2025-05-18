package com.bookmyroom.repository;

import com.bookmyroom.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    List<Booking> findByResourceId(String resourceId);
    List<Booking> findByCompanyId(String companyId);
    List<Booking> findByUserIdAndStartTimeBetween(String userId, LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> findByResourceIdAndStartTimeBetween(String resourceId, LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> findByResourceIdAndEndTimeAfter(String resourceId, LocalDateTime time);
} 