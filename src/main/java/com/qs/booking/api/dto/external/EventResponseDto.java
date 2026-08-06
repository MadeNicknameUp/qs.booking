package com.qs.booking.api.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EventResponseDto {

    private String id;

    private String pictureUrl;

    private String name;

    private String description;

    private String startingDate;

    private String endingDate;

    private String authorId;

    private Integer spotsAmount;

    private String creationTimestamp;

    private String updateTimestamp;
}
