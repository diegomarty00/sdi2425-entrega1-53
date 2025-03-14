package com.uniovi.sdi.sdi2425entrega153.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

// Refuel.java
@Entity
public class Refuel {
    @Id
    @GeneratedValue
    private Long id;
    private String vehicleRegistration;
    private String stationName;
    private double fuelPrice;
    private double fuelQuantity;
    private boolean fullTank;
    private double odometer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    private String observations;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public double getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(double fuelPrice) {
        this.fuelPrice = fuelPrice;
    }
    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }
    public double getFuelQuantity() {
        return fuelQuantity;
    }

    public void setFuelQuantity(double fuelQuantity) {
        this.fuelQuantity = fuelQuantity;
    }

    public boolean isFullTank() {
        return fullTank;
    }

    public void setFullTank(boolean fullTank) {
        this.fullTank = fullTank;
    }


    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

}