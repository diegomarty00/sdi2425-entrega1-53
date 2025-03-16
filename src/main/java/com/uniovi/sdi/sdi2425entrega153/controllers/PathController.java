package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import com.uniovi.sdi.sdi2425entrega153.services.UsersService;
import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import com.uniovi.sdi.sdi2425entrega153.validators.EndTripValidator;
import com.uniovi.sdi.sdi2425entrega153.validators.StartTripValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;
    private final UsersService usersService;
    private final HttpSession httpSession;
    private final VehicleService vehicleService;
    @Autowired
    private EndTripValidator endTripValidator;
    @Autowired
    private StartTripValidator startTripValidator;

    public PathController(PathService pathService,
                          UsersService usersService,
                          HttpSession httpSession,
                          VehicleService vehicleService) {
        this.pathService = pathService;
        this.usersService = usersService;
        this.httpSession = httpSession;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/start")
    public String showStartForm(Model model) {
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();
        model.addAttribute("availableVehicles", availableVehicles);
        model.addAttribute("path", new Path());
        return "path/start";
    }

    @PostMapping("/start")
    public String startPath(@ModelAttribute("path") Path path,
                            BindingResult result,
                            Principal principal,
                            Model model) {
        // Asigna el DNI del usuario obtenido del Principal
        path.setUserDni(principal.getName());
        // Valida el objeto 'path' usando el StartTripValidator
        startTripValidator.validate(path, result);
        if (result.hasErrors()) {
            model.addAttribute("availableVehicles", vehicleService.getAvailableVehicles());
            return "path/start";
        }
        pathService.startPath(path);
        return "redirect:/home";
    }
    @PostMapping("/end")
    public String endPath(
            @ModelAttribute("activePath") Path path,
            BindingResult result,
            Principal principal,
            Model model
    ) {
        endTripValidator.validate(path, result);
        if (result.hasErrors()) {
            model.addAttribute("activePath", pathService.getActivePathForUser(principal.getName()));
            return "path/end";
        }
        pathService.endPath(path);
        return "redirect:/path/personal";
    }




    /**
     * GET /path/list
     * Lista los trayectos personales del usuario autenticado.
     */
    @GetMapping("/list")
    public String getList(Model model, Pageable pageable, Principal principal) {
        Pageable pageableWithSize = PageRequest.of(pageable.getPageNumber(), 5);
        String dni = principal.getName();
        User user = usersService.getUserByDni(dni);
        Page<Path> paths = pathService.getPathsForUser(pageable, user);
        model.addAttribute("pathList", paths.getContent());
        model.addAttribute("page", paths);
        return "path/list";
    }

    @GetMapping("/end")
    public String showEndForm(Model model, Principal principal) {
        Path activePath = pathService.getActivePathForUser(principal.getName());
        if (activePath == null) {
            model.addAttribute("error", "No tienes un trayecto en curso para finalizar.");
            return "redirect:/home";
        }
        model.addAttribute("activePath", activePath);
        return "path/end";
    }
}
