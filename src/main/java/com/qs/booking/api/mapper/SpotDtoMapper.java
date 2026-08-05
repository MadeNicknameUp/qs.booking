package com.qs.booking.api.mapper;

import com.qs.booking.api.dto.external.EventRequestDto;
import com.qs.booking.api.dto.interservice.SpotOrderDto;
import com.qs.booking.store.model.Event;
import org.springframework.stereotype.Component;

@Component
public class SpotDtoMapper {

    public SpotOrderDto toInternalDto(Event event, EventRequestDto eventRequestDto) {

        return SpotOrderDto.builder()
                .spotsInToTal(eventRequestDto.getSpotsAmount())
                .pricePerSpot(eventRequestDto.getPricePerSpot())
                .event(event)
                .build();
    }
}
