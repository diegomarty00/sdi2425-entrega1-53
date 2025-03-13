package com.uniovi.sdi.sdi2425entrega153.repositories;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

    @Query("SELECT r FROM Path r WHERE r.vehicleRegistration = ?1 ORDER BY r.id ASC")
    Page<Vehicle> findAllByVehicle(Pageable pageable, Vehicle Vehicle);

    Page<Vehicle> findAll(Pageable pageable);
}