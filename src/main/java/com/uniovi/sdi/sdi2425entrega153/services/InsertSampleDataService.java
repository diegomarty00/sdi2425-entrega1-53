package com.uniovi.sdi.sdi2425entrega153.services;

import com.uniovi.sdi.sdi2425entrega153.entities.Path;
import com.uniovi.sdi.sdi2425entrega153.entities.Refuel;
import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class InsertSampleDataService {

    private final UsersService usersService;
    private final RolesService rolesService;

    private final VehicleService vehicleService;
    private final PathService pathService;
    private final RefuelService refuelService;

    public InsertSampleDataService(UsersService usersService, RolesService rolesService, VehicleService vehicleService, PathService pathService, RefuelService refuelService) {
        this.usersService = usersService;
        this.rolesService = rolesService;
        this.vehicleService = vehicleService;
        this.pathService = pathService;
        this.refuelService = refuelService;
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

        Vehicle v0 = new Vehicle("1111", "CHASIS", "Alfa", "Romeo", Vehicle.FUEL_TYPES.DIESEL, 4578);
        vehicleService.addVehicle(v0);
        Vehicle v1 = new Vehicle("1234BCD", "CHASIS12345678901234", "Alfa", "Romeo", Vehicle.FUEL_TYPES.DIESEL, 4578);
        v1.setFree(false);
        vehicleService.addVehicle(v1);
        Vehicle v2 = new Vehicle("O1234AB", "CHASIS23456789012345", "Toyota", "Corolla", Vehicle.FUEL_TYPES.GASOLINA, 12345);
        vehicleService.addVehicle(v2);
        Vehicle v3 = new Vehicle("5678EFG", "CHASIS34567890123456", "Honda", "Civic", Vehicle.FUEL_TYPES.HIBRIDO, 67890);
        vehicleService.addVehicle(v3);
        Vehicle v4 = new Vehicle("2345XYZ", "CHASIS45678901234567", "Ford", "Focus", Vehicle.FUEL_TYPES.ELECTRICO, 23456);
        vehicleService.addVehicle(v4);
        Vehicle v5 = new Vehicle("A1234BCD", "CHASIS56789012345678", "Volkswagen", "Golf", Vehicle.FUEL_TYPES.DIESEL, 78901);
        vehicleService.addVehicle(v5);

        Vehicle v6 = new Vehicle("1234BCM", "CHASIS12345678901231", "Alfa", "Romeo", Vehicle.FUEL_TYPES.DIESEL, 4578);
        vehicleService.addVehicle(v6);
        Vehicle v7 = new Vehicle("O1234AM", "CHASIS23456789012311", "Toyota", "Corolla", Vehicle.FUEL_TYPES.GASOLINA, 12345);
        vehicleService.addVehicle(v7);
        Vehicle v8 = new Vehicle("5678EFM", "CHASIS34567890123111", "Honda", "Civic", Vehicle.FUEL_TYPES.HIBRIDO, 67890);
        vehicleService.addVehicle(v8);
        Vehicle v9 = new Vehicle("2345XYM", "CHASIS45678901231111", "Ford", "Focus", Vehicle.FUEL_TYPES.ELECTRICO, 23456);
        vehicleService.addVehicle(v9);
        Vehicle v10 = new Vehicle("A1234BCM", "CHASIS567890111111", "Volkswagen", "Golf", Vehicle.FUEL_TYPES.DIESEL, 78901);
        vehicleService.addVehicle(v10);

        Vehicle v11 = new Vehicle("1234BCZ", "CHASIS12345678901230", "Alfa", "Romeo", Vehicle.FUEL_TYPES.DIESEL, 4578);
        vehicleService.addVehicle(v11);
        Vehicle v12= new Vehicle("O1234ZZ", "CHASIS23456789012310", "Toyota", "Corolla", Vehicle.FUEL_TYPES.GASOLINA, 12345);
        vehicleService.addVehicle(v12);
        Vehicle v13 = new Vehicle("5678EZZ", "CHASIS34567890123110", "Honda", "Civic", Vehicle.FUEL_TYPES.HIBRIDO, 67890);
        vehicleService.addVehicle(v13);
        Vehicle v14 = new Vehicle("2345XZZ", "CHASIS45678901231110", "Ford", "Focus", Vehicle.FUEL_TYPES.ELECTRICO, 23456);
        vehicleService.addVehicle(v14);

        // Trayectos
        Path path1 = new Path(new Date(), 1.2, 4578, 4600, "1111", 15.0, user1);
        path1.setUserDni(user1.getDni());
        pathService.addPath(path1);

        Path path2 = new Path(new Date(), 2.5, 12345, 12400, "1234BCD", 35.0, user2);
        path2.setUserDni(user1.getDni());
        pathService.addPath(path2);

        Path path3 = new Path(new Date(), 0.8, 67890, 68000, "5678EFG", 10.0, user3);
        path3.setUserDni(user3.getDni());
        pathService.addPath(path3);

        Path path4 = new Path(new Date(), 1.5, 23456, 23600, "2345XYZ", 20.0, user1);
        path4.setUserDni(user1.getDni());
        pathService.addPath(path4);

        Path path5 = new Path(new Date(), 3.0, 78901, 79000, "A1234BCD", 50.0, user2);
        path5.setUserDni(user3.getDni());
        pathService.addPath(path5);

        Refuel r1 = new Refuel("RedSol", 1.40, 30, 2142, new Date(), "", v0);
        refuelService.addRefuel(r1);

    }
}