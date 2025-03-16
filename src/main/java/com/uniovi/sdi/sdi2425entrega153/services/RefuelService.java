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

import java.util.Date;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Service
public class RefuelService {

    @Autowired
    private RefuelRepository refuelRepository;


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

    public RefuelService(RefuelRepository refuelRepository, HttpSession httpSession) {
        this.refuelRepository = refuelRepository;
        this.httpSession = httpSession;
    }
    public void addRefuel(Refuel path) {
        // Si en Id es null le asignamos el ultimo + 1 de la lista
        refuelRepository.save(path);
    }

    /**
     * Guarda el repostaje, asignando la fecha y hora actual.
     */
    public void saveRefuel(Refuel refuel){
            refuel.setDateTime(new Date());
            refuelRepository.save(refuel);
    }

    public Page<Refuel> findByVehiclePlate(String plate, Pageable pageable) {
        Page<Refuel> refuels = refuelRepository.findAllByVehiclePlate(pageable, plate);
        return refuels;
    }

    public List<Refuel> getRefulsByVehicle(String plate) {
        List<Refuel> result = refuelRepository.getRefulsByVehicle(plate);
        return result;
    }
}
