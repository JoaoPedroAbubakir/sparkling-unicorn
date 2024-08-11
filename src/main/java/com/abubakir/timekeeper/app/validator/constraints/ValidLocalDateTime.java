package com.abubakir.timekeeper.app.validator.constraints;

import com.abubakir.timekeeper.app.validator.LocalDateTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateTimeValidator.class)
public @interface ValidLocalDateTime {
    String message() default "Data e hora em formato inválido, confira se o formato segue: yyyy-MM-dd'T'HH:mm:ss";

    String pattern() default "yyyy-MM-dd'T'HH:mm:ss";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
