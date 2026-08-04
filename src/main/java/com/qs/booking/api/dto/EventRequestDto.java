package com.qs.booking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EventRequestDto {

    private String pictureUrl;

    private String name;

    private String description;

    private String startingDate;

    private String endingDate;

    private Integer spotsAmount;
}
