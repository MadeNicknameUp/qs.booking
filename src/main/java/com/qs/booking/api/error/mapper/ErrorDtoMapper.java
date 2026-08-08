package com.qs.booking.api.error.mapper;

import com.qs.booking.api.error.dto.CustomErrorResponse;
import com.qs.booking.api.error.unit.CustomException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ErrorDtoMapper {

    public static CustomErrorResponse toDto(CustomException ex) {

        return CustomErrorResponse.builder()
                .message(ex.getMessage())
                .path(ex.getPath())
                .status(ex.getCode().value())
                .timestamp(ex.getTimestamp())
                .build();
    }

    public static CustomErrorResponse toDto(Exception ex, Integer code) {

        return CustomErrorResponse.builder()
                .message(ex.getMessage())
                .path("")
                .status(code)
                .timestamp(Instant.now())
                .build();
    }
}
