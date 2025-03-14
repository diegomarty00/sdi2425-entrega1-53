package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import com.uniovi.sdi.sdi2425entrega153.services.RefuelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/refuel")
public class RefuelController {

    private final RefuelService refuelService;
    private final PathService pathService;

    public RefuelController(RefuelService refuelService, PathService pathService) {
        this.refuelService = refuelService;
        this.pathService = pathService;
    }

    /**
     * GET /refuel/new
     * Muestra el formulario para registrar un repostaje.
     */
    @GetMapping("/new")
    public String showRefuelForm(Model model, Principal principal) {
        // Se debe obtener el trayecto activo del usuario.


        String userDni = principal.getName();
        Path activePath = pathService.getActivePathForUser(userDni); // Método a implementar en PathService según tu lógica

        if(activePath == null) {
            model.addAttribute("error", "No tienes un trayecto en curso para registrar un repostaje.");
            return "redirect:/home";
        }

        model.addAttribute("activePath", activePath);
        model.addAttribute("refuel", new Refuel());
        return "refuel/new";
    }

    /**
     * POST /refuel/new
     * Procesa el formulario de repostaje.
     */
    @PostMapping("/new")
    public String registerRefuel(@ModelAttribute("refuel") Refuel refuel, BindingResult result,
                                 Principal principal, Model model) {
        String userDni = principal.getName();
        // Obtenemos trayecto activo del usuario
        Path activePath = pathService.getActivePathForUser(userDni); // Método a implementar o adaptar

        if(activePath == null) {
            result.reject("error.refuel", "No tienes un trayecto en curso para registrar un repostaje.");
            return "refuel/new";
        }

        // Validaciones:
        if(refuel.getStationName() == null || refuel.getStationName().trim().isEmpty()) {
            result.rejectValue("stationName", "error.stationName", "El nombre de la estación es obligatorio.");
        }
        if(refuel.getFuelPrice() <= 0) {
            result.rejectValue("fuelPrice", "error.fuelPrice", "El precio debe ser un número positivo.");
        }
        if(refuel.getFuelQuantity() <= 0) {
            result.rejectValue("fuelQuantity", "error.fuelQuantity", "La cantidad debe ser un número positivo.");
        }
        if(refuel.getOdometer() <= activePath.getInitialConsumption()) {
            result.rejectValue("odometer", "error.odometer", "El odómetro debe ser mayor que el inicio del trayecto.");
        }

        if(result.hasErrors()){
            model.addAttribute("activePath", activePath);
            return "refuel/new";
        }

        // Asigna la fecha y hora actual
        refuel.setDateTime(new Date());

        // Guarda el repostaje
        refuelService.saveRefuel(refuel);

        // Redirige, por ejemplo, a la lista de repostajes del vehículo
        return "redirect:/refuel/list?vehicle=" + activePath.getVehicleRegistration();
    }
}
