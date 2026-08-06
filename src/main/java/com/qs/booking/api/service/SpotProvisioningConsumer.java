package com.qs.booking.api.service;

import com.qs.booking.api.dto.interservice.SpotOrderDto;
import com.qs.booking.store.model.Spot;
import com.qs.booking.store.model.SpotState;
import com.qs.booking.store.repository.SpotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotProvisioningConsumer {

    private final SpotRepository spotRepository;

    @Transactional
    @RabbitListener(queues = {"${rabbitmq.spot-queue.name}"})
    public void receive(SpotOrderDto spotOrderDto) {

        log.info("Received Order Message from spot-queue: " + spotOrderDto);

        List<Spot> newSpots = new ArrayList<>(spotOrderDto.getSpotsInToTal());

        for (int i = 0; i < spotOrderDto.getSpotsInToTal(); ++i) {

            newSpots.add(createNewSpot(spotOrderDto));
        }

        spotRepository.saveAll(newSpots);
    }

    private Spot createNewSpot(SpotOrderDto spotOrderDto) {

        Spot spot = new Spot();
        spot.setPrice(spotOrderDto.getPricePerSpot());
        spot.setEventId(spotOrderDto.getEventId());
        spot.setState(SpotState.AVAILABLE);
        return spot;
    }
}
