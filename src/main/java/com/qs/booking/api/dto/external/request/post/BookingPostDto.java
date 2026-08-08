package com.qs.booking.api.dto.external.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BookingPostDto {

    @NotBlank
    private String spotId;

    @NotBlank
    private String idempotencyKey;
}
