package com.uniovi.sdi.sdi2425entrega153.repositories;

import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RefuelRepository extends CrudRepository<Refuel, Long> {

    @Query("SELECT r FROM Refuel r WHERE r.vehicleRegistration = ?1 ORDER BY r.id ASC")
    Page<Refuel> findAllByVehicle(Pageable pageable, Vehicle Vehicle);

    Page<Refuel> findAll(Pageable pageable);
}