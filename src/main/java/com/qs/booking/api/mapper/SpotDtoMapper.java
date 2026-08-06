package com.qs.booking.api.mapper;

import com.qs.booking.api.dto.external.EventRequestDto;
import com.qs.booking.api.dto.interservice.SpotOrderDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpotDtoMapper {

    public SpotOrderDto toInternalDto(UUID eventId, EventRequestDto eventRequestDto) {

        return SpotOrderDto.builder()
                .spotsInToTal(eventRequestDto.getSpotsAmount())
                .pricePerSpot(eventRequestDto.getPricePerSpot())
                .eventId(eventId)
                .build();
    }
}
