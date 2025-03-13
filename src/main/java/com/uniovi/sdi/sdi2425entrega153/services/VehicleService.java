package com.uniovi.sdi.sdi2425entrega153.services;

import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import com.uniovi.sdi.sdi2425entrega153.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Page<Vehicle> findAll(Pageable pageable) {
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

}
