package com.qs.booking.api.dto.interservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class SpotOrderDto {

    private Integer spotsInToTal;

    private BigDecimal pricePerSpot;

    private UUID eventId;
}
