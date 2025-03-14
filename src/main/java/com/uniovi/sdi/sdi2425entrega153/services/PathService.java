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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;

@Service
public class PathService {
    /* Inyección de dependencias basada en campos (opción no recomendada) */
    @Autowired
    private PathRepository pathRepository;

    private final PathRepository pathRepository;
    /* Inyección de dependencias basada en constructor (opción recomendada)*/
    private final HttpSession httpSession;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;

    public PathService(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    @Autowired
    public PathService(HttpSession httpSession) {
        this.httpSession = httpSession;
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
        // Esta implementación depende de que la entidad Path tenga el campo "userDni".
        // Ejemplo:
        // return pathRepository.findByUserDniAndFinalConsumption(userDni, 0).orElse(null);
        // Si no tienes ese campo, debes adaptar la lógica para identificar el trayecto activo del usuario.
        return pathRepository.findByUserDniAndFinalConsumption(userDni, 0).orElse(null);
    public Page<Path> getPaths(Pageable pageable) {
        Page<Path> paths = pathRepository.findAll(pageable);
        return paths;
    }

    public Path getPath(Long id) {
        Path path = pathRepository.findById(id).isPresent() ? pathRepository.findById(id).get() : new Path();
        return path;
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
        if(path.getUser().getDni().equals(dni) ) {
            pathRepository.updateResend(revised, id);
    /**
     * Finaliza un trayecto activo.
     * Se actualiza el campo finalConsumption, se calcula el tiempo transcurrido y la distancia recorrida.
     *
     * @param pathId        Identificador del trayecto a finalizar.
     * @param finalOdometer Valor ingresado para el odómetro final.
     * @param observations  Observaciones que podrían almacenarse o usarse para el log.
     */
    public void endPath(Long pathId, double finalOdometer, String observations) {
        Optional<Path> optPath = pathRepository.findById(pathId);
        if (!optPath.isPresent()) {
            throw new IllegalArgumentException("Trayecto no encontrado.");
        }
    }

    public void addPath(Path path) {
        // Si en Id es null le asignamos el ultimo + 1 de la lista
        Path path = optPath.get();
        if (finalOdometer <= path.getInitialConsumption()) {
            throw new IllegalArgumentException("El odómetro final debe ser mayor que el inicial.");
        }
        // Actualiza el trayecto con el odómetro final
        path.setFinalConsumption(finalOdometer);
        // Calcula la duración en segundos del trayecto
        long durationInSeconds = (new Date().getTime() - path.getStartDate().getTime()) / 1000;
        path.setTime(durationInSeconds);
        // Calcula los kilómetros recorridos
        double kilometers = finalOdometer - path.getInitialConsumption();
        path.setKilometers(kilometers);
        // Si deseas almacenar observaciones, tendrías que agregar un campo en la entidad Path.
        // Aquí se podría loggear o procesar la observación de otra forma.
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
        path.setFinalConsumption(0);
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
