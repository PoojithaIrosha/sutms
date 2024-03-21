package com.poojithairosha.ejb.remote;

import com.poojithairosha.core.model.DeviceData;
import jakarta.ejb.Remote;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Remote
public interface IoTDeviceData {

    DeviceData saveData(int vehicleSpeed, String trafficLightStatus, Map<String, Double> coordinates, Date captureTime);

    Map<String, Object> findDeviceDataPaginate(int page, String date, String location);

}
