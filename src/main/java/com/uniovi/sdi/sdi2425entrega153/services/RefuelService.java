package com.uniovi.sdi.sdi2425entrega153.services;

import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.repositories.RefuelRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefuelService {

    private final RefuelRepository refuelRepository;

    public RefuelService(RefuelRepository refuelRepository) {
        this.refuelRepository = refuelRepository;
    }

    /**
     * Guarda el repostaje, asignando la fecha y hora actual.
     */
    public void saveRefuel(Refuel refuel) {
        refuel.setDateTime(new Date());
        refuelRepository.save(refuel);
    }
}
