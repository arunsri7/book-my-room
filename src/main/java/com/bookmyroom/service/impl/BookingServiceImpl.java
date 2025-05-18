package com.bookmyroom.service.impl;

import com.bookmyroom.exception.BookingConflictException;
import com.bookmyroom.exception.ResourceNotFoundException;
import com.bookmyroom.exception.UnauthorizedException;
import com.bookmyroom.model.Booking;
import com.bookmyroom.model.Resource;
import com.bookmyroom.model.User;
import com.bookmyroom.model.UserRole;
import com.bookmyroom.repository.BookingRepository;
import com.bookmyroom.security.CustomUserDetails;
import com.bookmyroom.service.BookingService;
import com.bookmyroom.service.ResourceService;
import com.bookmyroom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ResourceService resourceService;
    private final UserService userService;

    private User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

    @Override
    public List<Booking> getAllBookings() {
        User currentUser = getCurrentUser();
        log.debug("Fetching bookings for user: {} with role: {}", currentUser.getEmail(), currentUser.getRole());
        
        List<Booking> bookings;
        if (currentUser.getRole().equals(UserRole.SUPER_ADMIN)) {
            bookings = bookingRepository.findAll();
            log.debug("Super admin retrieved all {} bookings", bookings.size());
        } else if (currentUser.getRole().equals(UserRole.COMPANY_ADMIN)) {
            bookings = bookingRepository.findByCompanyId(currentUser.getCompanyId());
            log.debug("Company admin retrieved {} bookings for company {}", bookings.size(), currentUser.getCompanyId());
        } else {
            bookings = bookingRepository.findByUserId(currentUser.getId());
            log.debug("User retrieved {} of their own bookings", bookings.size());
        }
        
        return bookings;
    }

    @Override
    public Booking getBookingById(String id) {
        log.debug("Fetching booking with id: {} from repository", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Booking not found with id: {}", id);
                    return new ResourceNotFoundException("Booking not found with id: " + id);
                });
        
        User currentUser = getCurrentUser();
        log.debug("Checking authorization for user: {} with role: {}", currentUser.getEmail(), currentUser.getRole());
        
        if (currentUser.getRole().equals(UserRole.SUPER_ADMIN)) {
            log.debug("Super admin authorized to view booking");
        } else if (currentUser.getRole().equals(UserRole.COMPANY_ADMIN)) {
            if (!booking.getCompanyId().equals(currentUser.getCompanyId())) {
                log.warn("Company admin {} attempted to access booking from different company", currentUser.getEmail());
                throw new UnauthorizedException("You do not have permission to view this booking");
            }
            log.debug("Company admin authorized to view booking");
        } else {
            if (!booking.getUserId().equals(currentUser.getId())) {
                log.warn("User {} attempted to access booking of another user", currentUser.getEmail());
                throw new UnauthorizedException("You do not have permission to view this booking");
            }
            log.debug("User authorized to view their own booking");
        }
        
        log.debug("Retrieved booking: {}", booking);
        return booking;
    }

    @Override
    public Booking createBooking(Booking booking) {
        log.info("Starting booking creation process for resource: {} by user: {}", 
                booking.getResourceId(), booking.getUserId());
        
        log.debug("Validating resource availability for resource: {}", booking.getResourceId());
        Resource resource = resourceService.getResourceById(booking.getResourceId());
        if (!resource.isAvailable()) {
            log.warn("Resource {} is not available for booking", booking.getResourceId());
            throw new BookingConflictException("Resource is not available for booking");
        }
        log.debug("Resource {} is available for booking", booking.getResourceId());

        log.debug("Validating user access for user: {}", booking.getUserId());
        User user = userService.findById(booking.getUserId());
        if (user == null) {
            log.error("User not found with id: {}", booking.getUserId());
            throw new ResourceNotFoundException("User not found with id: " + booking.getUserId());
        }
        if (!resource.getCompanyId().equals(user.getCompanyId())) {
            log.warn("User {} does not have access to book resource {} (different company)", 
                    booking.getUserId(), booking.getResourceId());
            throw new UnauthorizedException("User does not have access to book this resource");
        }
        log.debug("User {} has access to resource {}", booking.getUserId(), booking.getResourceId());

    
        log.debug("Validating booking time slot for resource: {}", booking.getResourceId());
        validateBookingTime(booking);
        log.debug("Booking time slot is valid");

        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setCompanyId(resource.getCompanyId());
        log.debug("Setting booking status to CONFIRMED and company ID to {}", resource.getCompanyId());


        Booking savedBooking = bookingRepository.save(booking);
        log.info("Successfully created booking with id: {} for resource: {} by user: {}", 
                savedBooking.getId(), savedBooking.getResourceId(), savedBooking.getUserId());
        return savedBooking;
    }

    @Override
    public Booking updateBooking(String id, Booking booking) {
        log.info("Starting booking update process for booking id: {}", id);
        Booking existingBooking = getBookingById(id);
        
        log.debug("Validating resource availability for resource: {}", booking.getResourceId());
        Resource resource = resourceService.getResourceById(booking.getResourceId());
        if (!resource.isAvailable()) {
            log.warn("Resource {} is not available for booking", booking.getResourceId());
            throw new BookingConflictException("Resource is not available for booking");
        }
        log.debug("Resource {} is available for booking", booking.getResourceId());

        log.debug("Validating user access for user: {}", booking.getUserId());
        User user = userService.findById(booking.getUserId());
        if (user == null) {
            log.error("User not found with id: {}", booking.getUserId());
            throw new ResourceNotFoundException("User not found with id: " + booking.getUserId());
        }
        if (!resource.getCompanyId().equals(user.getCompanyId())) {
            log.warn("User {} does not have access to book resource {} (different company)", 
                    booking.getUserId(), booking.getResourceId());
            throw new UnauthorizedException("User does not have access to book this resource");
        }
        log.debug("User {} has access to resource {}", booking.getUserId(), booking.getResourceId());

        log.debug("Validating booking time slot for resource: {}", booking.getResourceId());
        validateBookingTime(booking);
        log.debug("Booking time slot is valid");
        
        existingBooking.setStartTime(booking.getStartTime());
        existingBooking.setEndTime(booking.getEndTime());
        existingBooking.setPurpose(booking.getPurpose());
        existingBooking.setUpdatedAt(LocalDateTime.now());
        log.debug("Updated booking details for booking id: {}", id);
        
        Booking updatedBooking = bookingRepository.save(existingBooking);
        log.info("Successfully updated booking with id: {} for resource: {} by user: {}", 
                updatedBooking.getId(), updatedBooking.getResourceId(), updatedBooking.getUserId());
        return updatedBooking;
    }

    @Override
    public void deleteBooking(String id) {
        log.info("Starting booking deletion process for booking id: {}", id);
        Booking booking = getBookingById(id);
        
        bookingRepository.deleteById(id);
        log.info("Successfully deleted booking with id: {} for resource: {} by user: {}", 
                id, booking.getResourceId(), booking.getUserId());
    }

    @Override
    public List<Booking> getUserBookings(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        User currentUser = getCurrentUser();
        log.debug("Checking authorization for user: {} with role: {} to access bookings for user: {}", 
                currentUser.getEmail(), currentUser.getRole(), userId);
        
        if (!currentUser.getRole().equals(UserRole.SUPER_ADMIN) && 
            !currentUser.getRole().equals(UserRole.COMPANY_ADMIN) && 
            !currentUser.getId().equals(userId)) {
            log.warn("User {} attempted to access bookings of another user {}", currentUser.getEmail(), userId);
            throw new UnauthorizedException("You do not have permission to view these bookings");
        }
        
        if (currentUser.getRole().equals(UserRole.COMPANY_ADMIN)) {
            User targetUser = userService.findById(userId);
            if (targetUser == null || !targetUser.getCompanyId().equals(currentUser.getCompanyId())) {
                log.warn("Company admin {} attempted to access bookings of user from different company", currentUser.getEmail());
                throw new UnauthorizedException("You do not have permission to view these bookings");
            }
        }
        
        log.debug("Fetching bookings for user: {} between {} and {}", userId, startTime, endTime);
        List<Booking> bookings;
        if (startTime != null && endTime != null) {
            bookings = bookingRepository.findByUserIdAndStartTimeBetween(userId, startTime, endTime);
            log.debug("Retrieved {} bookings for user {} within time range", bookings.size(), userId);
        } else {
            bookings = bookingRepository.findByUserId(userId);
            log.debug("Retrieved {} bookings for user {}", bookings.size(), userId);
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByResource(String resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Fetching bookings for resource: {} between {} and {}", resourceId, startTime, endTime);
        List<Booking> bookings;
        if (startTime != null && endTime != null) {
            bookings = bookingRepository.findByResourceIdAndStartTimeBetween(resourceId, startTime, endTime);
            log.debug("Retrieved {} bookings for resource {} within time range", bookings.size(), resourceId);
        } else {
            bookings = bookingRepository.findByResourceId(resourceId);
            log.debug("Retrieved {} bookings for resource {}", bookings.size(), resourceId);
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByCompany(String companyId) {
        log.debug("Fetching all bookings for company: {}", companyId);
        List<Booking> bookings = bookingRepository.findByCompanyId(companyId);
        log.debug("Retrieved {} bookings for company {}", bookings.size(), companyId);
        return bookings;
    }

    private void validateBookingTime(Booking booking) {
        log.debug("Starting booking time validation for resource: {}", booking.getResourceId());
        
        if (booking.getStartTime().isAfter(booking.getEndTime())) {
            log.warn("Invalid booking time: start time {} is after end time {}", 
                    booking.getStartTime(), booking.getEndTime());
            throw new BookingConflictException("Start time must be before end time");
        }

        if (booking.getStartTime().isBefore(LocalDateTime.now())) {
            log.warn("Invalid booking time: start time {} is in the past", booking.getStartTime());
            throw new BookingConflictException("Cannot book resources in the past");
        }

        log.debug("Checking for overlapping bookings for resource: {}", booking.getResourceId());
        List<Booking> existingBookings = bookingRepository.findByResourceIdAndStartTimeBetween(
                booking.getResourceId(),
                booking.getStartTime(),
                booking.getEndTime()
        );

        List<Booking> bookingsStartingBefore = bookingRepository.findByResourceIdAndEndTimeAfter(
                booking.getResourceId(),
                booking.getStartTime()
        );

        existingBookings.addAll(bookingsStartingBefore);
        existingBookings = existingBookings.stream()
                .distinct()
                .filter(b -> !b.getId().equals(booking.getId())) // Exclude current booking when updating
                .toList();

        for (Booking existingBooking : existingBookings) {
            if (booking.overlaps(existingBooking)) {
                log.warn("Booking conflict detected with existing booking: {} for resource: {}", 
                        existingBooking.getId(), booking.getResourceId());
                throw new BookingConflictException("Resource is already booked for this time period");
            }
        }
        log.debug("No booking conflicts found for resource: {}", booking.getResourceId());
    }
} 