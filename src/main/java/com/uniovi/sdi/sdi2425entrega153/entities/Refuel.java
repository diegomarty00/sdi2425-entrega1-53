package com.uniovi.sdi.sdi2425entrega153.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Refuel {
    @Id
    @GeneratedValue
    private Long id;

    // Campo para almacenar la matrícula del vehículo
    private String vehicleRegistration;


    private String stationName;
    private double fuelPrice;
    private double fuelQuantity;
    private boolean fullTank;
    private double odometer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    private String observations;


    @ManyToOne
    @JoinColumn(name = "vehicle_plate")
    private Vehicle vehicle;


    // Constructor vacío
    public Refuel() {}


    public Refuel(Long id, String stationName, double fuelPrice,
                  double fuelQuantity, double odometer, Date dateTime,
                  String observations) {
        this.id = id;
        this.stationName = stationName;
        this.fuelPrice = fuelPrice;
        this.fuelQuantity = fuelQuantity;
        this.odometer = odometer;
        this.dateTime = dateTime;
        this.observations = observations;
    }


    public Refuel(String stationName, double fuelPrice,
                  double fuelQuantity, double odometer, Date dateTime,
                  String observations, Vehicle vehicle) {
        super();

        this.stationName = stationName;
        this.fuelPrice = fuelPrice;
        this.fuelQuantity = fuelQuantity;
        this.odometer = odometer;
        this.dateTime = dateTime;
        this.observations = observations;
        this.vehicle = vehicle;
    }


    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
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

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
