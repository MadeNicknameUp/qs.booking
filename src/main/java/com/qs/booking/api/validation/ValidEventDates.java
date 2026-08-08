package com.qs.booking.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= EndingDateAfterStartingDateValidator.class)
public @interface ValidEventDates {
    String message() default "Ending date must be later then starting date.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
