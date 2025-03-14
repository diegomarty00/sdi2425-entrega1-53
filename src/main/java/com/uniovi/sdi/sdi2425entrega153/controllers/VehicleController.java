package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import com.uniovi.sdi.sdi2425entrega153.validators.VehicleRegistrationValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleRegistrationValidation vehicleRegistrationValidation;

    public VehicleController(VehicleService vehicleService, VehicleRegistrationValidation vehicleRegistrationValidation) {
        this.vehicleService = vehicleService;
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




    @RequestMapping(value = "/vehicle/delete", method = RequestMethod.POST)
    public String deleteVehicles(@RequestParam("selectedVehicles") List<String> plates) {
        vehicleService.deleteVehicles(plates);
        return "redirect:/vehicle/list";
    }


}
