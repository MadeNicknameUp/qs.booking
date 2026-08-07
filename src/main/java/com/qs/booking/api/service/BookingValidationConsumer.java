package com.qs.booking.api.service;

import com.qs.booking.api.dto.interservice.BookingOrderDto;
import com.qs.booking.api.error.unit.SpotNotFoundException;
import com.qs.booking.api.mapper.BookingDtoMapper;
import com.qs.booking.store.model.Booking;
import com.qs.booking.store.model.BookingState;
import com.qs.booking.store.model.Spot;
import com.qs.booking.store.model.SpotState;
import com.qs.booking.store.repository.BookingRepository;
import com.qs.booking.store.repository.SpotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingValidationConsumer {

    private final BookingRepository bookingRepository;
    private final SpotRepository spotRepository;
    private final BookingDtoMapper bookingDtoMapper;

    private final BookingValidationProducer bookingValidationProducer;

    @RabbitListener(queues= {"${rabbitmq.booking-queue.name}"})
    @Transactional
    public void receive(BookingOrderDto bookingOrderDto) {

        log.info("Received Order Message from spot-queue: " + bookingOrderDto);

        Spot orderedSpot = spotRepository.findById(bookingOrderDto.getSpotId())
                .orElseThrow(() -> new SpotNotFoundException("Spot with id: %s not found.".formatted(bookingOrderDto.getSpotId())));

        boolean isBooked = orderedSpot.getState().equals(SpotState.BOOKED);

        if(isBooked && bookingRepository.findBySpotId(orderedSpot.getId()).getIdempotencyKey()
                .equals(bookingOrderDto.getIdempotencyKey())) {
            return;
        }

        Booking booking = bookingDtoMapper.toEntity(bookingOrderDto);
        booking.setState(isBooked ? BookingState.REJECTED : BookingState.ACCEPTED);
        booking.setProcessedAt(Instant.now());

        final Booking savedBooking = bookingRepository.saveAndFlush(booking);

        bookingValidationProducer.postOrder(savedBooking);
    }
}
