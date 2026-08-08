package com.qs.booking.api.dto.external.request.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qs.booking.api.validation.ValidEventDates;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@ValidEventDates
@AllArgsConstructor
public class EventPostDto {

    private String pictureUrl;

    @NotBlank
    @Size(min= 4, max= 255)
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "Name contains invalid characters.")
    private String name;

    @Size(max= 2000)
    private String description;

    @NotBlank
    @FutureOrPresent
    private Instant startingDate;

    @NotBlank
    @Future
    private Instant endingDate;

    @NotNull
    @Min(4)
    @JsonFormat()
    private Integer spotsAmount;

    @NotNull
    @DecimalMin(value= "0.0")
    // For now it is permitted to make it free (incl. 0).
    private BigDecimal pricePerSpot;
}
