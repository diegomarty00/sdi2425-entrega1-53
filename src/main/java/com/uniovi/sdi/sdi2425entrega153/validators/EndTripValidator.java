package com.uniovi.sdi.sdi2425entrega153.validators;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EndTripValidator implements Validator {

    @Autowired
    private PathService pathService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Path.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Path path = (Path) target;

        if (path.getId() == null) {
            errors.reject("path.id.required", "No se ha indicado el trayecto a finalizar.");
            return;
        }

        Path dbPath = pathService.getPath(path.getId());
        if (dbPath == null) {
            errors.reject("path.notFound", "No se ha encontrado el trayecto indicado.");
            return;
        }

        // El empleado solo puede finalizar un trayecto si está en curso (finalConsumption == 0)
        if (dbPath.getFinalConsumption() != 0) {
            errors.reject("path.notActive", "No se puede finalizar un trayecto que no está en curso.");
        }

        // El odómetro es obligatorio y debe ser un número positivo superior al valor del odómetro al inicio del trayecto
        double finalOdometer = path.getFinalConsumption();
        if (finalOdometer <= 0) {
            errors.rejectValue("finalConsumption", "finalConsumption.positive", "El odómetro final debe ser un número positivo.");
        } else if (finalOdometer <= dbPath.getInitialConsumption()) {
            errors.rejectValue("finalConsumption", "finalConsumption.invalid",
                    "El odómetro final debe ser mayor que el inicial (" + dbPath.getInitialConsumption() + ").");
        }
    }
}
