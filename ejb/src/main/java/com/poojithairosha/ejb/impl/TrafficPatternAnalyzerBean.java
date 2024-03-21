package com.poojithairosha.ejb.impl;

import com.poojithairosha.core.model.DeviceData;
import com.poojithairosha.core.model.GPSCoordinates;
import com.poojithairosha.core.model.TrafficLightLocation;
import com.poojithairosha.core.util.HibernateUtil;
import com.poojithairosha.ejb.remote.TrafficPatternAnalyzer;
import jakarta.ejb.Stateless;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class TrafficPatternAnalyzerBean implements TrafficPatternAnalyzer {

    @Override
    public Map<TrafficLightLocation, Map<Integer, Integer>> analyze(String date) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDate localDate = LocalDate.parse(date);
            Timestamp startOfDay = Timestamp.valueOf(localDate.atStartOfDay());
            Timestamp endOfDay = Timestamp.valueOf(localDate.atTime(LocalTime.MAX));
            session.beginTransaction();
            List<DeviceData> dataList = session.createQuery("from DeviceData where captureTime >= :startDate and captureTime <= :endDate", DeviceData.class)
                    .setParameter("startDate", startOfDay)
                    .setParameter("endDate", endOfDay)
                    .getResultList();

            Map<TrafficLightLocation, Map<Integer, Integer>> vehicleCountByLocationHour = new HashMap<>();

            for (DeviceData data : dataList) {
                GPSCoordinates coordinates = data.getGpsCoordinates();
                TrafficLightLocation location = coordinates.getLocation();

                Instant instant = Instant.ofEpochMilli(data.getCaptureTime().getTime());
                LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                LocalTime captureTime = localDateTime1.toLocalTime();
                int hour = captureTime.getHour();

                vehicleCountByLocationHour.computeIfAbsent(location, k -> new HashMap<>())
                        .compute(hour, (k, v) -> v == null ? 1 : v + 1);
            }

            return vehicleCountByLocationHour;
        }
    }

}
