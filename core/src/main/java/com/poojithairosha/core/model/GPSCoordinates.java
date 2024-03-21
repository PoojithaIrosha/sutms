package com.poojithairosha.core.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "gps_coordinates")
public class GPSCoordinates implements Serializable {

    @Id
    private Long id;
    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    private TrafficLightLocation location;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    private DeviceData deviceData;

    public GPSCoordinates() {
    }

    public GPSCoordinates(double latitude, double longitude, TrafficLightLocation location) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public TrafficLightLocation getLocation() {
        return location;
    }

    public void setLocation(TrafficLightLocation location) {
        this.location = location;
    }

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(DeviceData deviceData) {
        this.deviceData = deviceData;
    }

    @Override
    public String toString() {
        return latitude + " - " + longitude + " - " + location;
    }
}
