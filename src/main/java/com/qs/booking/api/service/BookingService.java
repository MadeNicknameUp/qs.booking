package com.qs.booking.api.service;

import com.qs.booking.api.dto.external.request.post.BookingPostDto;
import com.qs.booking.api.dto.external.response.BookingResponseDto;
import com.qs.booking.api.error.unit.BookingNotFoundException;
import com.qs.booking.api.error.unit.InvalidParameterException;
import com.qs.booking.api.mapper.BookingDtoMapper;
import com.qs.booking.store.model.*;
import com.qs.booking.store.repository.BookingRepository;
import com.qs.booking.store.repository.SpotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SpotRepository spotRepository;
    private final AccountService accountService;
    private final BookingDtoMapper bookingDtoMapper;
    private final BookingProducer bookingProducer;

    public List<BookingResponseDto> getBookingHistory(UUID accountId, Integer pageNumber, Integer pageSize) {

        // TODO: Just use JWT tokens once I add them.
        Account fetchedAccount = accountService.internalFetchAccount(accountId)
                .orElseThrow(() -> new java.security.InvalidParameterException("Operation cannot be finished: Invalid account id."));

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Booking> bookingHistory = bookingRepository.findAllByPurchaserId(fetchedAccount.getId(), pageable);

        return bookingHistory
                .stream()
                .map(bookingDtoMapper::toDto)
                .toList();
    }

    public BookingResponseDto fetchBooking(UUID bookingId) {

        Booking fetchedBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Booking with id: %s not found.".formatted(bookingId),
                        "/api/v1/bookings/%s".formatted(bookingId)
                ));

        return bookingDtoMapper.toDto(fetchedBooking);
    }

    public void orderBooking(UUID accountId, BookingPostDto bookingPostDto) {

        UUID spotId = UUID.fromString(bookingPostDto.getSpotId());

        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new InvalidParameterException(
                        "Spot with id: %s not found.".formatted(spotId),
                        "/api/v1/bookings/%s".formatted(accountId)
                ));

        spot.setState(SpotState.PENDING);

        bookingProducer.postOrder(bookingDtoMapper.toDto(accountId, bookingPostDto));
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {

        Booking fetchedBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Booking with id: %s not found.".formatted(bookingId),
                        "/api/v1/bookings/%s".formatted(bookingId)
                ));

        bookingRepository.delete(fetchedBooking);
    }
}
