package com.qs.booking.api.contoller;

import com.qs.booking.api.dto.BookingRequestDto;
import com.qs.booking.api.dto.BookingResponseDto;
import com.qs.booking.api.service.BookingService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{account_id}")
    public ResponseEntity<List<BookingResponseDto>> getBookingHistory(
            @PathVariable UUID accountId,
            @RequestParam(name="page_number") Integer pageNumber,
            @RequestParam(name="page_size") Integer pageSize
    ) {

        return ResponseEntity.ok(bookingService.getBookingHistory(accountId, pageNumber, pageSize));
    }

    @GetMapping("/{booking_id}")
    public ResponseEntity<BookingResponseDto> fetchBooking(@PathVariable UUID bookingId) {

        return ResponseEntity.ok(bookingService.fetchBooking(bookingId));
    }

    @PostMapping("/{account_id}")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto bookingRequestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingService.createBooking(bookingRequestDto));
    }

    @DeleteMapping("/{booking_id}")
    public HttpStatus deleteAccount(@PathVariable UUID bookingId) {

        bookingService.deleteBooking(bookingId);

        return HttpStatus.OK;
    }
}
