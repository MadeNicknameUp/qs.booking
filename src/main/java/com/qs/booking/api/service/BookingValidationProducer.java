package com.qs.booking.api.service;

import com.qs.booking.api.mapper.BookingDtoMapper;
import com.qs.booking.store.model.Booking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingValidationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final BookingDtoMapper bookingDtoMapper;

    @Value("${rabbitmq.notification-queue.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    public void postOrder(Booking booking) {
        log.info(
                "Sending new Notification order for Booking with id: {}.",
                booking.getId()
        );

        rabbitTemplate.convertAndSend(exchange, routingKey, bookingDtoMapper.toDto(booking));
    }
}
