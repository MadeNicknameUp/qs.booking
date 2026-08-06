package com.qs.booking.api.dto.interservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class SpotOrderDto {

    private Integer spotsInToTal;

    private Double pricePerSpot;

    private UUID eventId;
}
