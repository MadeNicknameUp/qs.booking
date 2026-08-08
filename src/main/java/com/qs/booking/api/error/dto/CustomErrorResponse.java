package com.qs.booking.api.error.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CustomErrorResponse {

    private Integer status;

    private String message;

    private Instant timestamp;

    private String path;

//    private String errorCode;
}
