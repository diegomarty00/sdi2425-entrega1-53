package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import com.uniovi.sdi.sdi2425entrega153.entities.*;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import com.uniovi.sdi.sdi2425entrega153.services.UsersService;
import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import com.uniovi.sdi.sdi2425entrega153.validators.PathValidator;
import com.uniovi.sdi.sdi2425entrega153.validators.VehicleRegistrationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.LinkedList;

@Controller
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;
    private final UsersService usersService;
    private final HttpSession httpSession;
    private final VehicleService vehicleService; // Servicio para obtener vehículos disponibles

    @Autowired
    private PathValidator pathValidator;

    public PathController(PathService pathService, UsersService usersService, HttpSession httpSession) {
    public PathController(PathService pathService, VehicleService vehicleService) {
        this.pathService = pathService;
        this.vehicleService = vehicleService;
    }

    /**
     * GET /path/start
     * Muestra el formulario para iniciar un trayecto.
     */
    @GetMapping("/start")
    public String showStartForm(Model model) {
        // Obtenemos la lista de vehículos disponibles.
        // La lógica interna de VehicleService filtra los vehículos que no tengan un trayecto activo.
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();
        model.addAttribute("availableVehicles", availableVehicles);
        // Se envía un objeto vacío de Path que se rellenará en el formulario.
        model.addAttribute("path", new Path());
        return "path/start";
    }

    /**
     * POST /path/start
     * Procesa el formulario para iniciar un trayecto.
     */
    @PostMapping("/start")
    public String startPath(@ModelAttribute("path") Path path, BindingResult result, Principal principal, Model model) {
        // Validamos que se ha seleccionado un vehículo
        if (path.getVehicleRegistration() == null || path.getVehicleRegistration().trim().isEmpty()) {
            result.rejectValue("vehicleRegistration", "error.vehicleRegistration", "Debes seleccionar un vehículo");
            // Recargamos la lista de vehículos en caso de error
            model.addAttribute("availableVehicles", vehicleService.getAvailableVehicles());
            return "path/start";
        }
        this.usersService = usersService;
        this.httpSession = httpSession;
    }

    @RequestMapping("/path/personal")
    public String getList(Model model, Pageable pageable, Principal principal){
        String dni = principal.getName();
        User user = usersService.getUserByDni(dni);
        Page<Path> paths = pathService.getPathsForUser(pageable, user);
        model.addAttribute("pathList", paths.getContent());
        model.addAttribute("page", paths);
        return "path/personal";

        if (pathService.hasActivePathForVehicle(path.getVehicleRegistration())) {
            result.reject("error.path", "El vehículo seleccionado ya tiene un trayecto activo.");
            model.addAttribute("availableVehicles", vehicleService.getAvailableVehicles());
            return "path/start";
        }

        // Iniciar el trayecto:
        // - Se asigna la fecha de inicio (se realiza en el servicio).
        // - Se determina el odómetro inicial a partir del último trayecto finalizado para ese vehículo.
        pathService.startPath(path);


        return "redirect:/home";
    }
    @GetMapping("/end")
    public String showEndForm(Model model, Principal principal) {
        // Lógica para obtener el trayecto activo y agregarlo al modelo
        // Por ejemplo:
        Path activePath = pathService.getActivePathForUser(principal.getName());
        if (activePath == null) {
            model.addAttribute("error", "No tienes un trayecto en curso para finalizar.");
            return "redirect:/home";
        }
        model.addAttribute("activePath", activePath);
        return "path/end"; // Nombre de la vista
    }
}
