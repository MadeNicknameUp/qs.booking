package com.qs.booking.api.service;

import com.qs.booking.api.dto.external.BookingRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.booking-queue.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    public void postOrder(BookingRequestDto bookingRequestDto) {
        log.info(
                "Sending new Order with key: {} for spot with id: {}.",
                bookingRequestDto.getIdempotencyKey(),
                bookingRequestDto.getSpotId()
        );

        rabbitTemplate.convertAndSend(exchange, routingKey, bookingRequestDto);
    }

}
