package com.qs.booking.api.service;

import com.qs.booking.api.dto.interservice.BookingOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.booking-queue.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    public void postOrder(BookingOrderDto bookingOrderDto) {
        log.info(
                "Sending new Booking with key: {} for spot with id: {}.",
                bookingOrderDto.getIdempotencyKey(),
                bookingOrderDto.getSpotId()
        );

        rabbitTemplate.convertAndSend(exchange, routingKey, bookingOrderDto);
    }

}
