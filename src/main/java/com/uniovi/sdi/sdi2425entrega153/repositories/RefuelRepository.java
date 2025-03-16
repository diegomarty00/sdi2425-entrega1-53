package com.uniovi.sdi.sdi2425entrega153.repositories;

import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

// RefuelRepository.java
public interface RefuelRepository extends CrudRepository<Refuel, Long> {

    @Query("SELECT r FROM Refuel r WHERE r.vehicle.plate = ?1 ORDER BY r.id ASC")
    Page<Refuel> findAllByVehiclePlate(Pageable pageable, String plate);

    Page<Refuel> findAll(Pageable pageable);
}