package com.qs.booking.api.dto.external.request.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class BookingPostDto {

    private String spotId;

    private String idempotencyKey;
}
