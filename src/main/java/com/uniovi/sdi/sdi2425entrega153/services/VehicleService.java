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
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private PathService pathService; // Se inyecta para verificar si un vehículo está en uso

    public Page<Vehicle> findAll(Pageable pageable) {
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public Vehicle findByPlate(String plate) {
        return vehicleRepository.findByPlate(plate);
    }

    public Vehicle findByChassisNumber(String chassisNumber) {
        return vehicleRepository.findByChassisNumber(chassisNumber);
    }

    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles= new ArrayList<Vehicle>();
        vehicleRepository.findAll().forEach(vehicles::add);
        return vehicles;
    }

    /**
     * Obtiene la lista de vehículos disponibles.
     * Se filtran aquellos que no tienen un trayecto activo.
     *
     * @return Lista de vehículos disponibles.
     */
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> allVehicles = getVehicles();
        return allVehicles.stream()
                .filter(vehicle -> !pathService.hasActivePathForVehicle(vehicle.getPlate()))
                .collect(Collectors.toList());
    }

    public Page<Vehicle> findFree(Pageable pageable) {
        Page<Vehicle> vehicles = vehicleRepository.findFree(pageable);
        return vehicles;
    }
}
