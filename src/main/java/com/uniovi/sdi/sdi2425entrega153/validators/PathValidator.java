package com.uniovi.sdi.sdi2425entrega153.validators;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PathValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Path.class.equals(aClass);
    }
    @Override
    public void validate(Object target, Errors errors) {
        Path path = (Path) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description","Error.empty");
        if (path.getFinalConsumption() < 0) {
            errors.rejectValue("Final Consumption", "Error.path.consumption.final.negative");
        }
        if (path.getInitialConsumption() < 0) {
            errors.rejectValue("Initial Consumption", "Error.path.consumption.initial.negative");
        }
        if (path.getFinalConsumption() < path.getInitialConsumption()) {
            errors.rejectValue("Consumption", "Error.path.consumption.value.incorrect");
        }
    }
}