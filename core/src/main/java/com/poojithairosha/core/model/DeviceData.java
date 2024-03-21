package com.poojithairosha.core.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "device_data")
public class DeviceData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int vehicleSpeed;

    @Enumerated(EnumType.STRING)
    private TrafficLightStatus trafficLightStatus;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "deviceData")
    @PrimaryKeyJoinColumn
    private GPSCoordinates gpsCoordinates;

    @Temporal(TemporalType.TIMESTAMP)
    private Date captureTime;

    public DeviceData() {
    }

    public DeviceData(int vehicleSpeed, TrafficLightStatus trafficLightStatus, GPSCoordinates gpsCoordinates, Date captureTime) {
        this.vehicleSpeed = vehicleSpeed;
        this.trafficLightStatus = trafficLightStatus;
        this.gpsCoordinates = gpsCoordinates;
        this.captureTime = captureTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(int vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }

    public TrafficLightStatus getTrafficLightStatus() {
        return trafficLightStatus;
    }

    public void setTrafficLightStatus(TrafficLightStatus trafficLightStatus) {
        this.trafficLightStatus = trafficLightStatus;
    }

    public GPSCoordinates getGpsCoordinates() {
        return gpsCoordinates;
    }

    public void setGpsCoordinates(GPSCoordinates gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }

    public Date getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Date captureTime) {
        this.captureTime = captureTime;
    }

    @Override
    public String toString() {
        return "DeviceData{" +
                "id=" + id +
                ", vehicleSpeed=" + vehicleSpeed +
                ", trafficLightStatus=" + trafficLightStatus +
                ", gpsCoordinates=" + gpsCoordinates +
                ", captureTime=" + captureTime +
                '}';
    }
}
