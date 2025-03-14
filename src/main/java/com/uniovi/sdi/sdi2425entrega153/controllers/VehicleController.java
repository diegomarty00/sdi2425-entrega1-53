package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import com.uniovi.sdi.sdi2425entrega153.services.RefuelService;
import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import com.uniovi.sdi.sdi2425entrega153.validators.VehicleRegistrationValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleRegistrationValidation vehicleRegistrationValidation;
    private final PathService pathService;
    private final RefuelService refuelService;

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
        Page<Vehicle> vehicles = vehicleService.findAll(pageable);
        model.addAttribute("vehicles", vehicles.getContent());
        model.addAttribute("page", vehicles);
        return "vehicles/listVehicles";
    }

    @RequestMapping(value = "/vehicle/register", method = RequestMethod.POST)
    public String register(@Validated Vehicle vehicle, BindingResult result, Model model) { //@ModelAttribute Vehicle vehicle
        vehicleRegistrationValidation.validate(vehicle, result);

        if (result.hasErrors()) {

            model.addAttribute("fuelTypes", Vehicle.FUEL_TYPES.values());
            return "vehicles/registerVehicle";
        }

        vehicleService.addVehicle(vehicle);
        return "redirect:/vehicle/list";
    }

    @RequestMapping(value = "/vehicle/register", method = RequestMethod.GET)
    public String register(Model model) {

        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("fuelTypes", Vehicle.FUEL_TYPES.values());
        return "vehicles/registerVehicle";
    }

    @RequestMapping("/vehicle/list/update")
    public String update(Model model, Pageable pageable) {
        Page<Vehicle> vehicles = vehicleService.findAll(pageable);
        model.addAttribute("vehicles", vehicles.getContent());
        return "vehicle/list :: vehiclesTable";
    }

    @RequestMapping("/vehicle/details/{id}")
    public String getDetail(Model model, @PathVariable String id) {
        model.addAttribute("vehicle", vehicleService.findByPlate(id));
        return "vehicle/details";
    }

    @RequestMapping("/vehicle/paths/{id}")
    public String getVehicleTrips(Model model, @PathVariable String id, Pageable pageable) {
        Page<Path> paths = pathService.findByPlate(id, pageable);
        Vehicle vehicle = vehicleService.findByPlate(id);
        model.addAttribute("vehicle", vehicleService.findByPlate(id));
        model.addAttribute("paths", paths);
        return "vehicle/path";
    }

    @RequestMapping("/vehicle/refuels/{id}")
    public String getVehicleRefuels(Model model, @PathVariable String id, Pageable pageable) {
        Page<Refuel> refuels = refuelService.findByPlate(id, pageable);
        Vehicle vehicle = vehicleService.findByPlate(id);
        model.addAttribute("vehicle", vehicleService.findByPlate(id));
        model.addAttribute("refuels", refuels);
        return "vehicle/refuels";
    }

}
