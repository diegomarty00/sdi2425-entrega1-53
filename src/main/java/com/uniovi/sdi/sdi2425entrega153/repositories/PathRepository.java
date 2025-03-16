package com.uniovi.sdi.sdi2425entrega153.repositories;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Repository
public interface PathRepository extends CrudRepository<Path, Long> {

    // Busca un trayecto activo para un vehículo (finalConsumption == 0)
    Optional<Path> findByVehicleRegistrationAndFinalConsumption(String vehicleRegistration, double finalConsumption);
    @Query("SELECT r FROM Path r WHERE r.vehicleRegistration = ?1 ORDER BY r.id ASC")
    Page<Path> findAllByVehiclePlate(Pageable pageable, String plate);

    @Query("SELECT r FROM Path r  WHERE r.user = ?1 ORDER BY r.startDate ASC")
    Page<Path> findAllByUser(Pageable pageable, User user);

    @Query("SELECT r FROM Path r  WHERE r.user.dni = ?1 ORDER BY r.startDate ASC")
    List<Path> getPathsByUserDni(String dni);

    @Query("SELECT r FROM Path r WHERE r.vehicleRegistration = ?1  ORDER BY r.startDate ASC")
    List<Path> getPathsByVehicle(String plate);

    Page<Path> findAll(Pageable pageable);
    // Busca el último trayecto finalizado (finalConsumption > 0) para un vehículo, ordenado por startDate descendente.
    Optional<Path> findTopByVehicleRegistrationAndFinalConsumptionGreaterThanOrderByStartDateDesc(String vehicleRegistration, double finalConsumption);
    // Busca el trayecto activo para un usuario
    Optional<Path> findByUserDniAndFinalConsumption(String userDni, double finalConsumption);

    @Modifying
    @Transactional
    @Query("UPDATE Path SET resend = ?1 WHERE id = ?2")
    void updateResend(Boolean resend, Long id);



}