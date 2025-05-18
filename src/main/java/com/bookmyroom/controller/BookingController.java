package com.bookmyroom.controller;

import com.bookmyroom.model.Booking;
import com.bookmyroom.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings() {
        log.debug("Received request to get all bookings");
        List<Booking> bookings = bookingService.getAllBookings();
        log.debug("Returning {} bookings", bookings.size());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<Booking> getBookingById(@PathVariable String id) {
        log.debug("Received request to get booking with id: {}", id);
        Booking booking = bookingService.getBookingById(id);
        log.debug("Found booking: {}", booking);
        return ResponseEntity.ok(booking);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        log.info("Received request to create booking for resource: {} by user: {}", 
                booking.getResourceId(), booking.getUserId());
        Booking createdBooking = bookingService.createBooking(booking);
        log.info("Successfully created booking with id: {}", createdBooking.getId());
        return ResponseEntity.ok(createdBooking);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<Booking> updateBooking(@PathVariable String id, @Valid @RequestBody Booking booking) {
        log.info("Received request to update booking with id: {}", id);
        Booking updatedBooking = bookingService.updateBooking(id, booking);
        log.info("Successfully updated booking with id: {}", updatedBooking.getId());
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        log.info("Received request to cancel booking with id: {}", id);
        bookingService.deleteBooking(id);
        log.info("Successfully cancelled booking with id: {}", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<List<Booking>> getUserBookings(
            @PathVariable String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        log.debug("Received request to get bookings for user: {} between {} and {}", userId, startTime, endTime);
        List<Booking> bookings = bookingService.getUserBookings(userId, startTime, endTime);
        log.debug("Found {} bookings for user {}", bookings.size(), userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/resource/{resourceId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<List<Booking>> getBookingsByResource(@PathVariable String resourceId) {
        log.debug("Received request to get bookings for resource: {}", resourceId);
        List<Booking> bookings = bookingService.getBookingsByResource(resourceId, null, null);
        log.debug("Found {} bookings for resource {}", bookings.size(), resourceId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'USER')")
    public ResponseEntity<List<Booking>> getBookingsByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(bookingService.getBookingsByCompany(companyId));
    }
} 