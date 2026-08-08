package com.qs.booking.api.mapper;

import com.qs.booking.api.dto.external.request.post.EventPostDto;
import com.qs.booking.api.dto.interservice.SpotOrderDto;

import java.util.UUID;

public class SpotDtoMapper {

    public static SpotOrderDto toInternalDto(UUID eventId, EventPostDto eventPostDto) {

        return SpotOrderDto.builder()
                .spotsInToTal(eventPostDto.getSpotsAmount())
                .pricePerSpot(eventPostDto.getPricePerSpot())
                .eventId(eventId)
                .build();
    }
}
