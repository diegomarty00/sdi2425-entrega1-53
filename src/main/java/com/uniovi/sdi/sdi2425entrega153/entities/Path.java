package com.uniovi.sdi.sdi2425entrega153.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Path {
    @Id
    @GeneratedValue
    private Long id;                    // Identificador unico
    private Date startDate;             // Fecha y hora de inicio
    private double time;                // Tiempo del recorrido
    private double initialConsumption;  // Consumo antes del reccorido del recorrido
    private double finalConsumption;     // Consumo despues del recorrido
    private String vehicleRegistration; // Matricula del coche
    private double kilometers;          // Kilometros del trayecto

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Path(){}

    public Path(Long id, Date startDate, double time,
                double initialConsumption, double finalConsumption,
                String vehicleRegistration, double kilometers) {
        this.id = id;
        this.startDate = startDate;
        this.time = time;
        this.initialConsumption = initialConsumption;
        this.finalConsumption = finalConsumption;
        this.vehicleRegistration = vehicleRegistration;
        this.kilometers = kilometers;
    }

    public Path(Date startDate, double time,
                double initialConsumption, double finalConsumption,
                String vehicleRegistration, double kilometers, User user) {
        super();
        this.startDate = startDate;
        this.time = time;
        this.initialConsumption = initialConsumption;
        this.finalConsumption = finalConsumption;
        this.vehicleRegistration = vehicleRegistration;
        this.kilometers = kilometers;
        this.user = new User();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getInitialConsumption() {
        return initialConsumption;
    }

    public void setInitialConsumption(double initialConsumption) {
        this.initialConsumption = initialConsumption;
    }

    public double getFinalConsumption() {
        return finalConsumption;
    }

    public void setFinalConsumption(double finalConsumption) {
        this.finalConsumption = finalConsumption;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(id, path.id);
    }

    @Override
    public String toString() {
        return "Path{" + "id=" + id + " startDate=" + startDate +
                " time=" + time + " initialConsumption=" + initialConsumption +
                " finalConsumption=" + finalConsumption +
                " vehicleRegistration=" + vehicleRegistration +
                " kilometers=" + kilometers + "}";
    }
}
