package com.qs.booking.api.error;

import com.qs.booking.api.error.dto.CustomErrorResponse;
import com.qs.booking.api.error.mapper.ErrorDtoMapper;
import com.qs.booking.api.error.unit.*;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value= {
            AccountNotFoundException.class,
            BookingNotFoundException.class,
            EventNotFoundException.class,
            SpotNotFoundException.class,
            InvalidParameterException.class })
    public ResponseEntity<CustomErrorResponse> handleException(CustomException ex) {

        logError(ex);

        return ResponseEntity
                .status(ex.getCode())
                .body(ErrorDtoMapper.toDto(ex));
    }

    @ExceptionHandler(value= {
            IllegalArgumentException.class,
            ValidationException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<CustomErrorResponse> handleExceptions(Exception ex) {

        logError(ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDtoMapper.toDto(ex, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex) {

        logError(ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDtoMapper.toDto(ex, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    private void logError(Exception ex) {
        log.error("Error occurred: {} with message: {}.",
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
    }
}
