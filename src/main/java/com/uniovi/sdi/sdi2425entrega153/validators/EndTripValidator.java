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

        // 1. Comprobar que se ha indicado el ID del trayecto
        if (path.getId() == null) {
            errors.reject("path.id.required", "No se ha indicado el trayecto a finalizar.");
            return;
        }

        // 2. Recuperar el trayecto real de la BD
        Path dbPath = pathService.getPath(path.getId());
        if (dbPath == null) {
            errors.reject("path.notFound", "No se ha encontrado el trayecto indicado.");
            return;
        }

        // 3. Comprobar que está activo (finalConsumption == 0)
        if (dbPath.getFinalConsumption() != null && dbPath.getFinalConsumption() != 0) {
            errors.reject("path.notActive", "No se puede finalizar un trayecto que no está en curso.");
        }

        // 4. Ver si hay un error de conversión en finalConsumption (por ejemplo, campo vacío o texto no numérico)
        if (errors.hasFieldErrors("finalConsumption")) {
            // Con esto evitamos hacer más validaciones; el error de conversión ya está registrado
            return;
        }

        // 5. Validar el valor final una vez parseado correctamente
        Double finalOdometer = path.getFinalConsumption();
        if (finalOdometer == null) {
            errors.rejectValue("finalConsumption", "typeMismatch.finalConsumption");
        } else if (finalOdometer <= 0) {
            errors.rejectValue("finalConsumption", "finalConsumption.positive", "El odómetro final debe ser un número positivo.");
        } else if (finalOdometer <= dbPath.getInitialConsumption()) {
            errors.rejectValue("finalConsumption", "finalConsumption.invalid",
                    "El odómetro final debe ser mayor que el inicial (" + dbPath.getInitialConsumption() + ").");
        }
    }

}
