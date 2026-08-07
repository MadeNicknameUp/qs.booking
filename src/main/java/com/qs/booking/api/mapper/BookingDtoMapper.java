package com.qs.booking.api.mapper;

import com.qs.booking.api.dto.external.BookingRequestDto;
import com.qs.booking.api.dto.external.BookingResponseDto;
import com.qs.booking.api.dto.interservice.BookingOrderDto;
import com.qs.booking.api.error.unit.SpotNotFoundException;
import com.qs.booking.store.model.Booking;
import com.qs.booking.store.model.BookingState;
import com.qs.booking.store.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingDtoMapper {

    private final SpotRepository spotRepository;

    public BookingResponseDto toDto(Booking booking) {

        return BookingResponseDto
                .builder()
                .id(booking.getId().toString())
                .state(booking.getState().toString())
                .processedAt(booking.getProcessedAt().toString())
                .createdAt(booking.getCreatedAt().toString())
                .updatedAt(booking.getUpdatedAt().toString())
                .build();
    }

    public BookingOrderDto toDto(UUID accountId, BookingRequestDto bookingRequestDto) {

        return BookingOrderDto
                .builder()
                .purchaserId(accountId)
                .spotId(UUID.fromString(bookingRequestDto.getSpotId()))
                .idempotencyKey(UUID.fromString(bookingRequestDto.getIdempotencyKey()))
                .build();
    }

    // TODO: Evaluate practical use of this method.
    public Booking toEntity(BookingRequestDto bookingRequestDto) {

        Booking booking = new Booking();
        booking.setState(BookingState.PROCESSING);
        booking.setIdempotencyKey(UUID.fromString(bookingRequestDto.getIdempotencyKey()));
        booking.setSpot(spotRepository.findById(UUID.fromString(bookingRequestDto.getSpotId()))
                .orElseThrow(() -> new SpotNotFoundException("Spot with id %s not found.".formatted(bookingRequestDto.getSpotId())))
        );

        return booking;
    }

    public Booking toEntity(BookingOrderDto bookingOrderDto) {

        Booking booking = new Booking();
        booking.setState(BookingState.PROCESSING);
        booking.setIdempotencyKey(bookingOrderDto.getIdempotencyKey());
        booking.setPurchaserId(bookingOrderDto.getPurchaserId());
        booking.setSpot(spotRepository.findById(bookingOrderDto.getSpotId())
                .orElseThrow(() -> new SpotNotFoundException("Spot with id %s not found.".formatted(bookingOrderDto.getSpotId())))
        );

        return booking;
    }
}
