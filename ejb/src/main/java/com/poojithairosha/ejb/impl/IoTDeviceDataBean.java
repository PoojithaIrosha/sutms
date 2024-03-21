package com.poojithairosha.ejb.impl;

import com.poojithairosha.core.model.DeviceData;
import com.poojithairosha.core.model.GPSCoordinates;
import com.poojithairosha.core.model.TrafficLightLocation;
import com.poojithairosha.core.model.TrafficLightStatus;
import com.poojithairosha.ejb.remote.IoTDeviceData;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import com.poojithairosha.core.util.HibernateUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Singleton
public class IoTDeviceDataBean implements IoTDeviceData {

    @Override
    public DeviceData saveData(int vehicleSpeed, String trafficLightStatus, Map<String, Double> coordinates, Date captureTime) {
        GPSCoordinates gpsCoordinates = new GPSCoordinates(coordinates.get("latitude"), coordinates.get("longitude"), TrafficLightLocation.valueOf(String.valueOf(coordinates.get("location"))));
        DeviceData deviceData = new DeviceData(vehicleSpeed, TrafficLightStatus.valueOf(trafficLightStatus), gpsCoordinates, captureTime);
        gpsCoordinates.setDeviceData(deviceData);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(deviceData);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        System.out.println("Data saved");
        return deviceData;
    }
}
