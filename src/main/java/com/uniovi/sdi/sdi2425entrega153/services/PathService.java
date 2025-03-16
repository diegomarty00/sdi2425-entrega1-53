package com.uniovi.sdi.sdi2425entrega153.services;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.*;
import com.uniovi.sdi.sdi2425entrega153.repositories.PathRepository;
import com.uniovi.sdi.sdi2425entrega153.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;

@Service
public class PathService {

    private final PathRepository pathRepository;
    private final HttpSession httpSession;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public PathService(PathRepository pathRepository, HttpSession httpSession,
                        VehicleRepository vehicleRepository) {
        this.pathRepository = pathRepository;
        this.httpSession = httpSession;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Verifica si existe un trayecto activo para el vehículo dado.
     * Se considera activo aquel trayecto cuyo finalConsumption es 0.
     *
     * @param vehicleRegistration Matrícula del vehículo.
     * @return true si existe un trayecto activo; false de lo contrario.
     */
    public boolean hasActivePathForVehicle(String vehicleRegistration) {
        Optional<Path> activePath = pathRepository.findByVehicleRegistrationAndFinalConsumption(vehicleRegistration, 0);
        return activePath.isPresent();
    }
    /**
     * Método para obtener el trayecto activo del usuario.
     *
     *
     * @param userDni DNI del usuario.
     * @return Trayecto activo o null si no existe.
     */
    public Path getActivePathForUser(String userDni) {

        return pathRepository.findByUserDniAndFinalConsumption(userDni, 0).orElse(null);
    }
    public Page<Path> getPaths(Pageable pageable) {
        Page<Path> paths = pathRepository.findAll(pageable);
        return paths;
    }

    public Path getPath(Long id) {
        Path path = pathRepository.findById(id).isPresent() ? pathRepository.findById(id).get() : new Path();
        return path;
    }
    /**
     * Obtiene el último odómetro finalizado para un vehículo.
     * Si no existe trayecto finalizado, retorna 0.
     *
     * @param vehicleRegistration Matrícula del vehículo.
     * @return Valor del odómetro final o 0.
     */
    public double getLastOdometerForVehicle(String vehicleRegistration) {
        return pathRepository.findTopByVehicleRegistrationAndFinalConsumptionGreaterThanOrderByStartDateDesc(vehicleRegistration, 0)
                .map(Path::getFinalConsumption)
                .orElse(0.0);
    }

    public void setPathResend(boolean revised, Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Path path = pathRepository.findById(id).get();
        if (path.getUser().getDni().equals(dni)) {
            pathRepository.updateResend(revised, id);
        }
    }
    public Path createActivePathForVehicle(String vehicleRegistration, String userDni) {
        Path path = new Path();
        path.setVehicleRegistration(vehicleRegistration);
        path.setUserDni(userDni);
        path.setStartDate(new Date());
        double initialOdometer = getLastOdometerForVehicle(vehicleRegistration);
        path.setInitialConsumption(initialOdometer);
        path.setFinalConsumption(0.0);
        pathRepository.save(path);
        return path;
    }
    /**
     * Finaliza un trayecto activo.
     * Se actualiza el campo finalConsumption, se calcula el tiempo transcurrido y la distancia recorrida.
     *
     * @param path
     */
    public void endPath(Path path) {
        // Recuperar desde la BD el path real
        Path dbPath = pathRepository.findById(path.getId())
                .orElseThrow(() -> new IllegalArgumentException("Trayecto no encontrado."));

        // Actualizar campos, por ejemplo:
        dbPath.setFinalConsumption(path.getFinalConsumption());
        dbPath.setObservations(path.getObservations());
        dbPath.setEndDate(new Date());

        // Guardar
        pathRepository.save(dbPath);
    }


    public void addPath(Path path) {

        pathRepository.save(path);
    }
    /**
     * Inicia un nuevo trayecto para un vehículo.
     * Se asigna la fecha actual como fecha de inicio, se calcula el odómetro inicial
     * a partir del último trayecto finalizado (si existe) y se marca el trayecto como activo
     * (finalConsumption = 0).
     *
     * @param path Trayecto a iniciar (debe tener asignada la matrícula del vehículo).
     */
    public void startPath(Path path) {
        path.setStartDate(new Date());
        double initialOdometer = getLastOdometerForVehicle(path.getVehicleRegistration());
        path.setInitialConsumption(initialOdometer);
        // Establece finalConsumption en 0 para indicar que el trayecto está activo.
        path.setFinalConsumption(0.0);
        pathRepository.save(path);
    }

    public void deletePath(Long id) {
        pathRepository.deleteById(id);
    }

    public Page<Path> getPathsForUser(Pageable pageable, User user) {
        Page<Path> paths = new PageImpl<Path>(new LinkedList<Path>());
        paths = pathRepository.findAllByUser(pageable, user);
        return paths;
    }



    public Page<Path> findByVehiclePlate(String plate, Pageable pageable) {
        Page<Path> paths = pathRepository.findAllByVehiclePlate(pageable, plate);
        return paths;
    }
}
