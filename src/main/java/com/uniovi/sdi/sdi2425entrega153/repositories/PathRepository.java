package com.uniovi.sdi.sdi2425entrega153.repositories;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PathRepository extends CrudRepository<Path, Long> {

    @Query("SELECT r FROM Path r WHERE r.vehicleRegistration = ?1 ORDER BY r.id ASC")
    Page<Path> findAllByVehicle(Pageable pageable, Vehicle Vehicle);

    @Query("SELECT r FROM Path r  WHERE r.user = ?1 ORDER BY r.startDate ASC")
    Page<Path> findAllByUser(Pageable pageable, User user);

    Page<Path> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Path SET resend = ?1 WHERE id = ?2")
    void updateResend(Boolean resend, Long id);
}