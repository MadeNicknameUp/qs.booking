package com.qs.booking.api.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class BookingRequestDto {

    private String spotId;

    private String idempotencyKey;
}
