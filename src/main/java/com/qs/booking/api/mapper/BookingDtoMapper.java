package com.qs.booking.api.mapper;

import com.qs.booking.api.dto.external.request.post.BookingPostDto;
import com.qs.booking.api.dto.external.response.BookingResponseDto;
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

    public BookingOrderDto toDto(UUID accountId, BookingPostDto bookingPostDto) {

        return BookingOrderDto
                .builder()
                .purchaserId(accountId)
                .spotId(UUID.fromString(bookingPostDto.getSpotId()))
                .idempotencyKey(UUID.fromString(bookingPostDto.getIdempotencyKey()))
                .build();
    }

    // TODO: Evaluate practical use of this method.
    public Booking toEntity(BookingPostDto bookingPostDto, String errorPath) {

        Booking booking = new Booking();
        booking.setState(BookingState.PROCESSING);
        booking.setIdempotencyKey(UUID.fromString(bookingPostDto.getIdempotencyKey()));
        booking.setSpot(spotRepository.findById(UUID.fromString(bookingPostDto.getSpotId()))
                .orElseThrow(() -> new SpotNotFoundException(
                        "Spot with id %s not found.".formatted(bookingPostDto.getSpotId()),
                        errorPath
                ))
        );

        return booking;
    }

    public Booking toEntity(BookingOrderDto bookingOrderDto, String errorPath) {

        Booking booking = new Booking();
        booking.setState(BookingState.PROCESSING);
        booking.setIdempotencyKey(bookingOrderDto.getIdempotencyKey());
        booking.setPurchaserId(bookingOrderDto.getPurchaserId());
        booking.setSpot(spotRepository.findById(bookingOrderDto.getSpotId())
                .orElseThrow(() -> new SpotNotFoundException(
                        "Spot with id %s not found.".formatted(bookingOrderDto.getSpotId()),
                        errorPath
                ))
        );

        return booking;
    }
}
