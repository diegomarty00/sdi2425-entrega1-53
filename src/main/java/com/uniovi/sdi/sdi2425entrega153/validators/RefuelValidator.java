package com.uniovi.sdi.sdi2425entrega153.validators;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.repositories.PathRepository;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class RefuelValidator implements Validator {

    @Autowired
    private PathService pathService;

    @Autowired
    private PathRepository pathRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Refuel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Refuel refuel = (Refuel) target;

        if (refuel.getVehicle() == null) {
            errors.rejectValue("vehicle", "vehicle.required", "Debes asociar un vehículo al repostaje.");
        } else {
            String plate = refuel.getVehicle().getPlate();
            if (!pathService.hasActivePathForVehicle(plate)) {
                errors.reject("noActiveTrip", "No existe un trayecto en curso para este vehículo. No se puede repostar.");
            } else {
                Optional<Path> activePathOpt = pathRepository.findByVehicleRegistrationAndFinalConsumption(plate, 0);
                if (activePathOpt.isPresent()) {
                    Path activePath = activePathOpt.get();
                    if (refuel.getOdometer() <= activePath.getInitialConsumption()) {
                        errors.rejectValue("odometer", "odometer.invalid",
                                "El odómetro debe ser mayor que el valor inicial del trayecto (" + activePath.getInitialConsumption() + ").");
                    }
                }
            }
        }

        if (refuel.getStationName() == null || refuel.getStationName().trim().isEmpty()) {
            errors.rejectValue("stationName", "stationName.required", "El nombre de la estación es obligatorio.");
        } else if (!refuel.getStationName().equals(refuel.getStationName().trim())) {
            errors.rejectValue("stationName", "stationName.trim", "No puede contener espacios al principio o final.");
        }
        if (refuel.getFuelPrice() <= 0) {
            errors.rejectValue("fuelPrice", "fuelPrice.positive", "El precio debe ser un número positivo.");
        }
        if (refuel.getFuelQuantity() <= 0) {
            errors.rejectValue("fuelQuantity", "fuelQuantity.positive", "La cantidad debe ser un número positivo.");
        }
    }
}
