package com.qs.booking.api.dto.external.request.patch;

import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class EventPatchDto {

    private JsonNullable<String> pictureUrl;

    private JsonNullable<String> name;

    private JsonNullable<String> description;

    private JsonNullable<Instant> startingDate;

    private JsonNullable<Instant> endingDate ;

    // Place a ticket for a fix.
//    private JsonNullable<Integer> spotsAmount;

    private JsonNullable<BigDecimal> pricePerSpot;
}
