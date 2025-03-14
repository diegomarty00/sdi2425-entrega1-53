package com.uniovi.sdi.sdi2425entrega153.services;

import com.uniovi.sdi.sdi2425entrega153.entities.*;
import com.uniovi.sdi.sdi2425entrega153.repositories.PathRepository;
import com.uniovi.sdi.sdi2425entrega153.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;

@Service
public class PathService {
    /* Inyección de dependencias basada en campos (opción no recomendada) */
    @Autowired
    private PathRepository pathRepository;

    /* Inyección de dependencias basada en constructor (opción recomendada)*/
    private final HttpSession httpSession;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    public PathService(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Page<Path> getPaths(Pageable pageable) {
        Page<Path> paths = pathRepository.findAll(pageable);
        return paths;
    }

    public Path getPath(Long id) {
        Path path = pathRepository.findById(id).isPresent() ? pathRepository.findById(id).get() : new Path();
        return path;
    }

    public void setPathResend(boolean revised, Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Path path = pathRepository.findById(id).get();
        if(path.getUser().getDni().equals(dni) ) {
            pathRepository.updateResend(revised, id);
        }
    }

    public void addPath(Path path) {
        // Si en Id es null le asignamos el ultimo + 1 de la lista
        pathRepository.save(path);
    }

    public void deletePath(Long id) {
        pathRepository.deleteById(id);
    }

    public Page<Path> getPathsForUser(Pageable pageable, User user) {
        Page<Path> paths = new PageImpl<Path>(new LinkedList<Path>());
        if (user.getRole().equals("ROLE_STUDENT")) {
            paths = pathRepository.findAllByUser(pageable, user);}
        if (user.getRole().equals("ROLE_PROFESSOR")) {
            paths = getPaths(pageable); }
        return paths;
    }


    public Page<Path> findByPlate(String id, Pageable pageable) {
        Vehicle vehicle = vehicleRepository.findByPlate(id);
        Page<Path> paths = pathRepository.findAllByVehicle(pageable, vehicle);
        return paths;
    }
}
