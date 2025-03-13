package com.uniovi.sdi.sdi2425entrega153.repositories;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PathRepository extends JpaRepository<Path, Long> {

    // Busca un trayecto activo para un vehículo (finalConsumption == 0)
    Optional<Path> findByVehicleRegistrationAndFinalConsumption(String vehicleRegistration, double finalConsumption);

    // Busca el último trayecto finalizado (finalConsumption > 0) para un vehículo, ordenado por startDate descendente.
    Optional<Path> findTopByVehicleRegistrationAndFinalConsumptionGreaterThanOrderByStartDateDesc(String vehicleRegistration, double finalConsumption);
    // Busca el trayecto activo para un usuario
    Optional<Path> findByUserDniAndFinalConsumption(String userDni, double finalConsumption);
}
