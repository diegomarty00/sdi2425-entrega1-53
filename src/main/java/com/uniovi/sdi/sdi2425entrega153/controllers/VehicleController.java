package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.services.LogService;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import com.uniovi.sdi.sdi2425entrega153.services.RefuelService;
import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import com.uniovi.sdi.sdi2425entrega153.validators.VehicleRegistrationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleRegistrationValidation vehicleRegistrationValidation;
    private final PathService pathService;
    private final RefuelService refuelService;

    @Autowired
    private LogService logService; //inyectamos LogService

    public VehicleController(VehicleService vehicleService,
                             VehicleRegistrationValidation vehicleRegistrationValidation,
                             PathService pathService, RefuelService refuelService) {
        this.vehicleService = vehicleService;
        this.pathService = pathService;
        this.refuelService = refuelService;
        this.vehicleRegistrationValidation = vehicleRegistrationValidation;
    }

    @RequestMapping("/vehicle/list")
    public String list(Model model, Pageable pageable) {

        Pageable pageableWithSize = PageRequest.of(pageable.getPageNumber(), 5);
        Page<Vehicle> vehicles = vehicleService.findAll(pageableWithSize);

        model.addAttribute("vehicles", vehicles.getContent());
        model.addAttribute("page", vehicles);
        return "vehicle/listVehicles";
    }


    @RequestMapping("/vehicle/free")
    public String listVehicleFree(Model model, Pageable pageable) {
        Pageable pageableWithSize = PageRequest.of(pageable.getPageNumber(), 5);
        Page<Vehicle> vehicles = vehicleService.findFree(pageable);
        model.addAttribute("vehicles", vehicles.getContent());
        model.addAttribute("page", vehicles);
        return "vehicle/listFreeVehicles";
    }

    @RequestMapping("/vehicle/free/update")
    public String updateFree(Model model, Pageable pageable) {
        Page<Vehicle> vehicles = vehicleService.findFree(pageable);
        model.addAttribute("vehicles", vehicles.getContent());
        return "vehicle/free :: vehiclesTable";
    }

    @RequestMapping(value = "/vehicle/register", method = RequestMethod.POST)
    public String register(@Validated Vehicle vehicle, BindingResult result, Model model) { //@ModelAttribute Vehicle vehicle
        vehicleRegistrationValidation.validate(vehicle, result);

        if (result.hasErrors()) {

            model.addAttribute("fuelTypes", Vehicle.FUEL_TYPES.values());
            return "vehicle/registerVehicle";
        }

        vehicleService.addVehicle(vehicle);

        //obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        //guardar el log
        logService.saveLog(username, "REGISTER_VEHICLE", "/vehicle/register");

        return "redirect:/vehicle/list";
    }

    @RequestMapping(value = "/vehicle/register", method = RequestMethod.GET)
    public String register(Model model) {

        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("fuelTypes", Vehicle.FUEL_TYPES.values());
        return "vehicle/registerVehicle";
    }

    @RequestMapping("/vehicle/list/update")
    public String update(Model model, Pageable pageable) {
        Page<Vehicle> vehicles = vehicleService.findAll(pageable);
        model.addAttribute("vehicles", vehicles.getContent());
        return "vehicle/list :: vehiclesTable";
    }

    @RequestMapping("/vehicle/paths/{id}")
    public String getVehiclePaths(Model model, @PathVariable Long id, Pageable pageable) {
        Vehicle vehicle = vehicleService.getVehicle(id);
        Page<Path> paths = pathService.findByVehiclePlate(vehicle.getPlate(), pageable);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("paths", paths.getContent());
        model.addAttribute("page", paths);
        return "vehicle/paths";
    }

    @RequestMapping("/vehicle/refuels/{id}")
    public String getVehicleRefuels(Model model, @PathVariable Long id, Pageable pageable) {
        Vehicle vehicle = vehicleService.getVehicle(id);
        Page<Refuel> refuels = refuelService.findByVehiclePlate(vehicle.getPlate(), pageable);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("refuels", refuels.getContent());
        model.addAttribute("page", refuels);
        return "vehicle/refuels";
    }

    @RequestMapping(value = "/vehicle/delete", method = RequestMethod.POST)
    public String deleteVehicles(@RequestParam("selectedVehicles") List<String> plates) {
        vehicleService.deleteVehicles(plates);
        return "redirect:/vehicle/list";
    }


}
