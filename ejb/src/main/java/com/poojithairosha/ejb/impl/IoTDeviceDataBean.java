package com.poojithairosha.ejb.impl;

import com.poojithairosha.core.model.DeviceData;
import com.poojithairosha.core.model.GPSCoordinates;
import com.poojithairosha.core.model.TrafficLightLocation;
import com.poojithairosha.core.model.TrafficLightStatus;
import com.poojithairosha.ejb.remote.IoTDeviceData;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import com.poojithairosha.core.util.HibernateUtil;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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

    @Lock(LockType.READ)
    @Override
    public Map<String, Object> findDeviceDataPaginate(int page, String date, String location) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            List<Predicate> predicatesList = new ArrayList<>();
            List<Predicate> countPredicatesList = new ArrayList<>();

            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            JpaCriteriaQuery<DeviceData> query = criteriaBuilder.createQuery(DeviceData.class);

            // Join DeviceData and GPSCoordinates tables
            Root<DeviceData> deviceRoot = query.from(DeviceData.class);
            Join<DeviceData, GPSCoordinates> gpsCoordinateJoin = deviceRoot.join(
                    "gpsCoordinates", JoinType.INNER); // Use INNER JOIN for filtering

            JpaCriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<DeviceData> countRoot = countQuery.from(DeviceData.class);
            Join<DeviceData, GPSCoordinates> countGpsCoordinateJoin = countRoot.join(
                    "gpsCoordinates", JoinType.INNER);

            if (date != null) {
                LocalDate localDate = LocalDate.parse(date);
                Timestamp startOfDay = Timestamp.valueOf(localDate.atStartOfDay());
                Timestamp endOfDay = Timestamp.valueOf(localDate.atTime(LocalTime.MAX));

                predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(deviceRoot.get("captureTime"), startOfDay));
                predicatesList.add(criteriaBuilder.lessThanOrEqualTo(deviceRoot.get("captureTime"), endOfDay));

                countPredicatesList.add(criteriaBuilder.greaterThanOrEqualTo(countRoot.get("captureTime"), startOfDay));
                countPredicatesList.add(criteriaBuilder.lessThanOrEqualTo(countRoot.get("captureTime"), endOfDay));
            }

            if (location != null) {
                predicatesList.add(criteriaBuilder.equal(gpsCoordinateJoin.get("location"), location));
                countPredicatesList.add(criteriaBuilder.equal(countGpsCoordinateJoin.get("location"), location));
            }

            int pageCount = 0;
            int limit = 200;
            Long count = null;

            if (predicatesList.isEmpty()) {
                countQuery.select(criteriaBuilder.count(countRoot));
                count = session.createQuery(countQuery).uniqueResult();
                pageCount = (int) Math.ceil((double) count / limit);
            } else {
                countQuery.select(criteriaBuilder.count(countRoot)).where(criteriaBuilder.and(countPredicatesList.toArray(new Predicate[0])));
                count = session.createQuery(countQuery).uniqueResult();
                pageCount = (int) Math.ceil((double) count / limit);
            }

            List<DeviceData> deviceDataList = null;

            if (predicatesList.isEmpty()) {
                query.select(deviceRoot);
            } else {
                query.select(deviceRoot).where(criteriaBuilder.and(predicatesList.toArray(new Predicate[0])));
            }

            deviceDataList = session.createQuery(query)
                    .setFirstResult((page > 0 ? page - 1 : 0) * limit)
                    .setMaxResults(limit)
                    .getResultList();

            session.getTransaction().commit();

            Map<String, Object> result = new HashMap<>();
            result.put("count", pageCount);
            result.put("deviceDataList", deviceDataList);
            result.put("page", page);
            result.put("date", date);

            return result;
        }
    }
}
