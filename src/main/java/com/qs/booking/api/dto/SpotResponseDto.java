package com.qs.booking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class SpotResponseDto {

    private String id;

    private Integer price;

    private String state;

    private Instant createdAt;

    private Instant updatedAt;
}
