package com.lucasvila.tp_api.validation.validator;

import com.lucasvila.tp_api.validation.anotation.ValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
//VALIDACION PERSONALIZADA
public class ValidNameValidator implements ConstraintValidator<ValidName, String > {

    private static final String NAME_REGEX = "^[a-zA-Z]+$";

    @Override
    public void initialize(ValidName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true; // O false, dependiendo de cómo quieras manejar los valores null
        }
        return  value.matches(NAME_REGEX);
    }


}
