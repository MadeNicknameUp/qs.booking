package com.qs.booking.api.service;

import com.qs.booking.api.dto.external.BookingRequestDto;
import com.qs.booking.api.dto.external.BookingResponseDto;
import com.qs.booking.api.error.unit.BookingNotFoundException;
import com.qs.booking.api.error.unit.InvalidCreationRequestException;
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

import java.security.InvalidParameterException;
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

        // TODO: Replace with gRPC later on.
        Account fetchedAccount = accountService.internalFetchAccount(accountId)
                .orElseThrow(() -> new InvalidParameterException("Operation cannot be finished: Invalid account id."));

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Booking> bookingHistory = bookingRepository.findAllByPurchaserId(fetchedAccount.getId(), pageable);

        return bookingHistory
                .stream()
                .map(bookingDtoMapper::toDto)
                .toList();
    }

    public BookingResponseDto fetchBooking(UUID bookingId) {

        Booking fetchedBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: %s not found.".formatted(bookingId)));

        return bookingDtoMapper.toDto(fetchedBooking);
    }

    public void orderBooking(BookingRequestDto bookingRequestDto) {

        UUID spotId = UUID.fromString(bookingRequestDto.getSpotId());

        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new InvalidCreationRequestException("Spot with id: %s not found.".formatted(spotId)));

        spot.setState(SpotState.PENDING);

        bookingProducer.postOrder(bookingRequestDto);
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {

        Booking fetchedBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: %s not found.".formatted(bookingId)));

        bookingRepository.delete(fetchedBooking);
    }
}
