package com.qs.booking.api.dto.interservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class BookingOrderDto {

    private UUID spotId;

    private UUID purchaserId;

    private UUID idempotencyKey;
}
