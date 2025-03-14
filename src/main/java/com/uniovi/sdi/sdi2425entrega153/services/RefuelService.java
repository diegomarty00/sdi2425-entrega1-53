package com.uniovi.sdi.sdi2425entrega153.services;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import com.uniovi.sdi.sdi2425entrega153.repositories.PathRepository;
import com.uniovi.sdi.sdi2425entrega153.repositories.RefuelRepository;
import com.uniovi.sdi.sdi2425entrega153.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;

@Service
public class RefuelService {
    /* Inyección de dependencias basada en campos (opción no recomendada) */
    @Autowired
    private RefuelRepository refuelRepository;

    /* Inyección de dependencias basada en constructor (opción recomendada)*/
    private final HttpSession httpSession;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    public RefuelService(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Page<Refuel> getRefuels(Pageable pageable) {
        Page<Refuel> refuels = refuelRepository.findAll(pageable);
        return refuels;
    }

    public Refuel getRefuel(Long id) {
        Refuel refuel = refuelRepository.findById(id).isPresent() ? refuelRepository.findById(id).get() : new Refuel();
        return refuel;
    }


    public void addRefuel(Refuel path) {
        // Si en Id es null le asignamos el ultimo + 1 de la lista
        refuelRepository.save(path);
    }



    public Page<Refuel> findByPlate(String id, Pageable pageable) {
        Vehicle vehicle = vehicleRepository.findByPlate(id);
        Page<Refuel> refuel = refuelRepository.findAllByVehicle(pageable, vehicle);
        return refuel;
    }
}
