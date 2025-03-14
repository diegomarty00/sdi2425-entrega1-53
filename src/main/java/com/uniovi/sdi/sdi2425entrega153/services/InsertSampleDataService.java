package com.uniovi.sdi.sdi2425entrega153.services;

import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class InsertSampleDataService {

    private final UsersService usersService;
    private final RolesService rolesService;

    private final VehicleService vehicleService;

    public InsertSampleDataService(UsersService usersService, RolesService rolesService, VehicleService vehicleService) {
        this.usersService = usersService;
        this.rolesService = rolesService;
        this.vehicleService = vehicleService;
    }

    @PostConstruct
    public void init() {
        User user1 = new User("99999990A", "Pedro", "Díaz");
        user1.setPassword("123456");
        user1.setRole(rolesService.getRoles()[0]);
        User user2 = new User("12345678Z", "Lucas", "Núñez");
        user2.setPassword("@Dm1n1str@D0r");
        user2.setRole(rolesService.getRoles()[1]);
        User user3 = new User("99999992C", "María", "Rodríguez");
        user3.setPassword("123456");
        user3.setRole(rolesService.getRoles()[0]);

        usersService.addUser(user1);
        usersService.addUser(user2);
        usersService.addUser(user3);

        Vehicle v1 = new Vehicle("1111", "CHASIS", "Alfa", "Romeo", Vehicle.FUEL_TYPES.DIESEL, 4578);
        vehicleService.addVehicle(v1);
    }
}