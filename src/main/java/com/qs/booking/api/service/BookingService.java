package com.qs.booking.api.service;

import com.qs.booking.api.dto.BookingRequestDto;
import com.qs.booking.api.dto.BookingResponseDto;
import com.qs.booking.api.error.unit.BookingNotFoundException;
import com.qs.booking.api.mapper.BookingDtoMapper;
import com.qs.booking.store.model.Account;
import com.qs.booking.store.model.Booking;
import com.qs.booking.store.model.BookingState;
import com.qs.booking.store.model.SpotState;
import com.qs.booking.store.repository.BookingRepository;
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
    private final AccountService accountService;
    private final BookingDtoMapper bookingDtoMapper;

    public List<BookingResponseDto> getBookingHistory(UUID accountId, Integer pageNumber, Integer pageSize) {

        // TODO: Replace with gRPC later on.

        Account fetchedAccount = accountService.internalFetchAccount(accountId)
                .orElseThrow(() -> new InvalidParameterException("Operation cannot be finished: Invalid account id."));

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Booking> bookingHistory = bookingRepository.findAllByPurchaser(fetchedAccount, pageable);

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

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {

        Booking mappedBooking = bookingDtoMapper.toEntity(bookingRequestDto);

        mappedBooking.setState(BookingState.PROCESSING);
        mappedBooking.getSpot().setState(SpotState.PENDING);

        final Booking createdBooking = bookingRepository.save(mappedBooking);

        // TODO: This is a provider. This methods is going to be posting into the queue.

        return bookingDtoMapper.toDto(createdBooking);
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {

        Booking fetchedBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: %s not found.".formatted(bookingId)));

        bookingRepository.delete(fetchedBooking);
    }
}
