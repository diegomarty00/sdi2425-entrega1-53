package com.uniovi.sdi.sdi2425entrega153.validators;

import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class VehicleRegistrationValidation implements Validator {

    private final VehicleService vehicleService;

    public VehicleRegistrationValidation(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

//    Todos los campos son obligatorios y no deben contener espacio en blanco al principio o al final
//    del texto.
//
//    - La matrícula del vehículo debe ser una matrícula con formato válido en España para vehículos
//    ordinarios (ejemplo, 1234BCD o O1234AB).
//
//    - La matrícula no podrá estar repetida en el sistema.
//
//     - El número de bastidor deberá tener una longitud válida, es decir 17 caracteres y no podrá estar
//    repetido en el sistema
//

    @Override
    public boolean supports(Class<?> aClass) {
        return Vehicle.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Vehicle vehicle = (Vehicle) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "plate", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "chassisNumber", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "brandName", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "model", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fuelType", "Error.empty");

        String plate = vehicle.getPlate().trim();

        //validar formato de matrícula (4 números + 3 letras)
        if (!plate.matches("^[0-9]{4}[A-Z]{3}$")) {
            errors.rejectValue("plate", "Error.vehicle.plate.format");
        }

        //validar que la matrícula no esté repetida en la base de datos
        if (vehicleService.findByPlate(plate) != null) {
            errors.rejectValue("plate", "Error.vehicle.plate.duplicate");
        }

        //validar número de bastidor (17 caracteres exactos)
        String chassisNumber = vehicle.getChassisNumber().trim();
        if (chassisNumber.length() != 17) {
            errors.rejectValue("chassisNumber", "Error.vehicle.chassisNumber.length");
        }

        if (vehicleService.findByChassisNumber(chassisNumber) != null) {
            errors.rejectValue("chassisNumber", "Error.vehicle.chassisNumber.repeated");
        }

    }
}
