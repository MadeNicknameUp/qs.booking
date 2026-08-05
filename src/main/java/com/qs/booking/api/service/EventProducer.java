package com.qs.booking.api.service;

import com.qs.booking.api.dto.interservice.SpotOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.spot-queue.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    public void postOrder(SpotOrderDto spotOrderDto) {
        log.info(
                "Ordering {} spots for event id: {}.",
                spotOrderDto.getSpotsInToTal(),
                spotOrderDto.getEvent().getId()
        );

        rabbitTemplate.convertAndSend(exchange, routingKey, spotOrderDto);
    }
}
