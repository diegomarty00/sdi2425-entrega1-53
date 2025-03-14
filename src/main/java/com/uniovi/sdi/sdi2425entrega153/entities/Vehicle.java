package com.uniovi.sdi.sdi2425entrega153.entities;

import javax.persistence.*;

@Entity
public class Vehicle {

    public enum FUEL_TYPES {
        GASOLINA, DIESEL, MICROHIBRIDO, HIBRIDO, ELECTRICO, GLP, GNL
    }

    @Id
    private String plate;           // Matrícula
    @Column(unique = true)
    private String chassisNumber;   // Número de bastidor
    private String brandName;       // Marca
    private String model;           // Modelo

    @Enumerated(EnumType.STRING)
    private FUEL_TYPES fuelType;

    public Vehicle() {}

    public Vehicle(String plate, String chassisNumber, String brandName, String model, FUEL_TYPES fuelType) {
        this.plate = plate;
        this.chassisNumber = chassisNumber;
        this.brandName = brandName;
        this.model = model;
        this.fuelType = fuelType;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public FUEL_TYPES getFuelType() {
        return fuelType;
    }

    public void setFuelType(FUEL_TYPES fuelType) {
        this.fuelType = fuelType;
    }

}
