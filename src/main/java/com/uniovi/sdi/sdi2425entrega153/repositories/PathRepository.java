package com.uniovi.sdi.sdi2425entrega153.repositories;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PathRepository extends CrudRepository<Path, Long> {

    // Busca un trayecto activo para un vehículo (finalConsumption == 0)
    Optional<Path> findByVehicleRegistrationAndFinalConsumption(String vehicleRegistration, double finalConsumption);
    @Query("SELECT r FROM Path r WHERE r.vehicleRegistration = ?1 ORDER BY r.id ASC")
    Page<Path> findAllByVehicle(Pageable pageable, Vehicle Vehicle);

    Page<Path> findAll(Pageable pageable);
    // Busca el último trayecto finalizado (finalConsumption > 0) para un vehículo, ordenado por startDate descendente.
    Optional<Path> findTopByVehicleRegistrationAndFinalConsumptionGreaterThanOrderByStartDateDesc(String vehicleRegistration, double finalConsumption);
    // Busca el trayecto activo para un usuario
    Optional<Path> findByUserDniAndFinalConsumption(String userDni, double finalConsumption);
}
