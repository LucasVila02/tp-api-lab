package com.lucasvila.tp_api.validation.anotation;

import com.lucasvila.tp_api.validation.validator.ValidNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ValidName {

    String message() default "{custom.validation.message.name}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
