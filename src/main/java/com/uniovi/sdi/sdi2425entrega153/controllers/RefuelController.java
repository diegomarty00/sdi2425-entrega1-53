package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import com.uniovi.sdi.sdi2425entrega153.services.RefuelService;
import com.uniovi.sdi.sdi2425entrega153.validators.RefuelValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RefuelValidator refuelValidator;

    @Autowired
    public RefuelController(RefuelService refuelService, PathService pathService, RefuelValidator refuelValidator) {
        this.refuelService = refuelService;
        this.pathService = pathService;
        this.refuelValidator = refuelValidator;
    }

    @GetMapping("/new")
    public String showRefuelForm(Model model, Principal principal) {
        String userDni = principal.getName();
        Path activePath = pathService.getActivePathForUser(userDni);
        if(activePath == null) {
            model.addAttribute("error", "No tienes un trayecto en curso para registrar un repostaje.");
            return "redirect:/home";
        }
        model.addAttribute("activePath", activePath);
        model.addAttribute("refuel", new Refuel());
        return "refuel/new";
    }

    @PostMapping("/new")
    public String registerRefuel(@ModelAttribute("refuel") Refuel refuel, BindingResult result,
                                 Principal principal, Model model) {
        String userDni = principal.getName();
        Path activePath = pathService.getActivePathForUser(userDni);
        if(activePath == null) {
            result.reject("error.refuel", "No tienes un trayecto en curso para registrar un repostaje.");
            return "refuel/new";
        }

        // Llamada al validador para validar el objeto refuel
        refuelValidator.validate(refuel, result);

        if(result.hasErrors()){
            model.addAttribute("activePath", activePath);
            return "refuel/new";
        }

        refuel.setDateTime(new Date());
        refuelService.saveRefuel(refuel);
        return "redirect:/refuel/list?vehicle=" + activePath.getVehicleRegistration();
    }
}
