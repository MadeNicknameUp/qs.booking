package com.qs.booking.api.mapper;

import com.qs.booking.api.dto.BookingRequestDto;
import com.qs.booking.api.dto.BookingResponseDto;
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
//                .processedAt(booking.getProcessedAt().toString())
//                .updatedAt(booking.getUpdatedAt().toString())
//                .createdAt(booking.getCreatedAt().toString())
                // TODO: Replace this later.
                .processedAt("")
                .updatedAt("")
                .createdAt("")
                .build();
    }

    public Booking toEntity(BookingRequestDto bookingRequestDto) {

        Booking booking = new Booking();
        booking.setState(BookingState.PROCESSING);
        booking.setIdempotencyKey(UUID.fromString(bookingRequestDto.getIdempotencyKey()));
        booking.setSpot(spotRepository.findById(UUID.fromString(bookingRequestDto.getSpotId()))
                .orElseThrow(() -> new SpotNotFoundException("Spot with id %s not found.".formatted(bookingRequestDto.getSpotId())))
        );

        return booking;
    }
}
