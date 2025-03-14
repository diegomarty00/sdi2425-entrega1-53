package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import com.uniovi.sdi.sdi2425entrega153.services.UsersService;
import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import com.uniovi.sdi.sdi2425entrega153.validators.PathValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private final VehicleService vehicleService; // Servicio para obtener vehículos disponibles

    @Autowired
    private PathValidator pathValidator;

    public PathController(PathService pathService,
                          UsersService usersService,
                          HttpSession httpSession,
                          VehicleService vehicleService) {
        this.pathService = pathService;
        this.usersService = usersService;
        this.httpSession = httpSession;
        this.vehicleService = vehicleService;
    }

    /**
     * GET /path/start
     * Muestra el formulario para iniciar un trayecto.
     */
    @RequestMapping("/start")
    public String showStartForm(Model model) {
        // Obtenemos la lista de vehículos disponibles (aquellos que no tienen un trayecto activo).
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
    @RequestMapping("/start")
    public String startPath(@ModelAttribute("path") Path path,
                            BindingResult result,
                            Principal principal,
                            Model model) {

        // Validar que el usuario seleccione un vehículo
        if (path.getVehicleRegistration() == null || path.getVehicleRegistration().trim().isEmpty()) {
            result.rejectValue("vehicleRegistration",
                    "error.vehicleRegistration",
                    "Debes seleccionar un vehículo");
            model.addAttribute("availableVehicles", vehicleService.getAvailableVehicles());
            return "path/start";
        }

        // Validar que el vehículo no tenga un trayecto activo
        if (pathService.hasActivePathForVehicle(path.getVehicleRegistration())) {
            result.reject("error.path",
                    "El vehículo seleccionado ya tiene un trayecto activo.");
            model.addAttribute("availableVehicles", vehicleService.getAvailableVehicles());
            return "path/start";
        }

        // Iniciar el trayecto (la fecha de inicio y el odómetro inicial se establecen en el servicio)
        pathService.startPath(path);

        return "redirect:/home";
    }

    /**
     * GET /path/personal
     * Lista los trayectos personales del usuario autenticado.
     */
    @GetMapping("/personal")
    public String getList(Model model, Pageable pageable, Principal principal) {
        String dni = principal.getName();
        User user = usersService.getUserByDni(dni);
        Page<Path> paths = pathService.getPathsForUser(pageable, user);
        model.addAttribute("pathList", paths.getContent());
        model.addAttribute("page", paths);
        return "path/personal";
    }

    /**
     * GET /path/end
     * Muestra el formulario para finalizar un trayecto.
     */
    @RequestMapping("/end")
    public String showEndForm(Model model, Principal principal) {
        // Se busca el trayecto activo para el usuario
        Path activePath = pathService.getActivePathForUser(principal.getName());
        if (activePath == null) {
            model.addAttribute("error",
                    "No tienes un trayecto en curso para finalizar.");
            return "redirect:/home";
        }
        model.addAttribute("activePath", activePath);
        return "path/end"; // Vista para finalizar trayecto
    }
}
