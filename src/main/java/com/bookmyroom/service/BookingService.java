package com.bookmyroom.service;

import com.bookmyroom.model.Booking;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings();
    Booking getBookingById(String id);
    Booking createBooking(Booking booking);
    Booking updateBooking(String id, Booking booking);
    void deleteBooking(String id);
    List<Booking> getUserBookings(String userId, LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> getBookingsByResource(String resourceId, LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> getBookingsByCompany(String companyId);
} 