package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @RequestMapping("/vehicle/list")
    public String list(Model model, Pageable pageable) {
        Page<Vehicle> vehicles = vehicleService.findAll(pageable);
        model.addAttribute("vehicles", vehicles.getContent());
        model.addAttribute("page", vehicles);
        return "vehicles/listVehicles";
    }

    @RequestMapping(value = "/vehicle/register", method = RequestMethod.POST)
    public String register(@ModelAttribute Vehicle vehicle) {
        //meter validacion

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



}
