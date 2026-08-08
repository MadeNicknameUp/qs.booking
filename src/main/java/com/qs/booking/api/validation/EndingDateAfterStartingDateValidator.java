package com.qs.booking.api.validation;

import com.qs.booking.api.dto.external.request.post.EventPostDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndingDateAfterStartingDateValidator implements ConstraintValidator<ValidEventDates, EventPostDto> {

    @Override
    public boolean isValid(EventPostDto request, ConstraintValidatorContext context) {
        if (request.getStartingDate() == null || request.getEndingDate() == null) {
            return true; // @NotNull can catch it anyway
        }
        return request.getEndingDate().isAfter(request.getStartingDate());
    }
}
