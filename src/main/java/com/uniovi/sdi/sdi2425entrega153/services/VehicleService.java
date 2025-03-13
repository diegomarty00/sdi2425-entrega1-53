package com.uniovi.sdi.sdi2425entrega153.services;

import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import com.uniovi.sdi.sdi2425entrega153.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final PathService pathService; // Para verificar si un vehículo está en uso

    public VehicleService(VehicleRepository vehicleRepository, PathService pathService) {
        this.vehicleRepository = vehicleRepository;
        this.pathService = pathService;
    }

    /**
     * Obtiene la lista completa de vehículos.
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * Obtiene la lista de vehículos disponibles.
     * Se filtran aquellos que no tienen un trayecto activo.
     *
     * @return Lista de vehículos disponibles.
     */
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        return allVehicles.stream()
                .filter(vehicle -> !pathService.hasActivePathForVehicle(vehicle.getPlate()))
                .collect(Collectors.toList());
    }
}
