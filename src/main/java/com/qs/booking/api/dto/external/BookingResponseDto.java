package com.qs.booking.api.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookingResponseDto {

    // hashed id
    private String id;

    private String state;

    private String processedAt;

    private String createdAt;

    private String updatedAt;
}
