package com.qs.booking.api.contoller;

import com.qs.booking.api.dto.external.request.post.BookingPostDto;
import com.qs.booking.api.dto.external.response.BookingResponseDto;
import com.qs.booking.api.service.BookingService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/history/{account_id}")
    public ResponseEntity<List<BookingResponseDto>> getBookingHistory(
            @PathVariable(name="account_id") UUID accountId,
            @RequestParam(name="page_number") Integer pageNumber,
            @RequestParam(name="page_size") Integer pageSize
    ) {

        return ResponseEntity.ok(bookingService.getBookingHistory(accountId, pageNumber, pageSize));
    }

    @GetMapping("/{booking_id}")
    public ResponseEntity<BookingResponseDto> fetchBooking(@PathVariable(name="booking_id") UUID bookingId) {

        return ResponseEntity.ok(bookingService.fetchBooking(bookingId));
    }

    @PostMapping("/{account_id}")
    public ResponseEntity<Void> createBooking(@PathVariable(name="account_id") UUID accountId, @RequestBody BookingPostDto bookingPostDto) {

        bookingService.orderBooking(accountId, bookingPostDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{booking_id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable(name="booking_id") UUID bookingId) {

        bookingService.deleteBooking(bookingId);

        return ResponseEntity.noContent().build();
    }
}
