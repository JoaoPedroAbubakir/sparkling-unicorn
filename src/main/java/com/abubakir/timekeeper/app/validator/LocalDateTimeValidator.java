package com.abubakir.timekeeper.app.validator;

import com.abubakir.timekeeper.app.exception.InvalidDateTimeException;
import com.abubakir.timekeeper.app.exception.RequiredFieldNotPopulatedException;
import com.abubakir.timekeeper.app.validator.constraints.ValidLocalDateTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, String> {

    private String pattern;
    private String message;

    @Override
    public void initialize(ValidLocalDateTime constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String localDateTimeString, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTimeString == null || localDateTimeString.isBlank()) {
            throw new RequiredFieldNotPopulatedException();
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String formattedDate = String.format(localDateTimeString, formatter);
            LocalDateTime.parse(formattedDate, formatter);
            return true;
        }
        catch (DateTimeParseException e) {
            throw new InvalidDateTimeException(message);
        }
    }
}
