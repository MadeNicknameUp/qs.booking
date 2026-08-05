package com.qs.booking.api.dto.interservice;

import com.qs.booking.store.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SpotOrderDto {

    private Integer spotsInToTal;

    private Double pricePerSpot;

    private Event event;
}
