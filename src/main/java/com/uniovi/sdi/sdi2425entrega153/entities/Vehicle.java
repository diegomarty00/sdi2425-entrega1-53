package com.uniovi.sdi.sdi2425entrega153.entities;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vehicle {


    public enum FUEL_TYPES {
        GASOLINA, DIESEL, MACROHIBRIDO, HIBRIDO, ELECTRICO, GLP, GNL
    }

    @Id
    private String plate;           // matricula
    private String chassisNumber;   // numero de bastidor


    public Vehicle(){}


    public Vehicle(String plate, String chassisNumber) {
        this.plate = plate;
        this.chassisNumber = chassisNumber;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

}