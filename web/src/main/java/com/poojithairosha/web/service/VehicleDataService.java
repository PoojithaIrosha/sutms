package com.poojithairosha.web.service;

import com.poojithairosha.core.model.DeviceData;
import com.poojithairosha.core.model.TrafficLightLocation;
import com.poojithairosha.core.util.DecimalFormatter;
import com.poojithairosha.core.util.HibernateUtil;
import com.poojithairosha.ejb.remote.VehicleSpeedCalculator;
import com.poojithairosha.web.dto.AvgSpeedDTO;
import jakarta.inject.Singleton;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Singleton
public class VehicleDataService {

    private static final Map<String, VehicleSpeedCalculator> CALCULATORS = new HashMap<>();
    private static final InitialContext CONTEXT;

    static {
        try {
            CONTEXT = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public AvgSpeedDTO getAvgVehicleSpeed(TrafficLightLocation location, int speed) {
        try {
            VehicleSpeedCalculator calculator = null;

            switch (location) {
                case KANDY:
                    calculator = getCalculatorObj(TrafficLightLocation.KANDY);
                    break;
                case PERADENIYA:
                    calculator = getCalculatorObj(TrafficLightLocation.PERADENIYA);
                    break;
                case PILIMATHALAWA:
                    calculator = getCalculatorObj(TrafficLightLocation.PILIMATHALAWA);
                    break;
                case KATUGASTOTA:
                    calculator = getCalculatorObj(TrafficLightLocation.KATUGASTOTA);
                    break;
            }

            if (calculator != null) {
                double avgSpeed = Double.parseDouble(DecimalFormatter.format(calculator.calculateAverageSpeed(speed)));
                return new AvgSpeedDTO(avgSpeed);
            } else {
                throw new RuntimeException("Calculator not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private VehicleSpeedCalculator getCalculatorObj(TrafficLightLocation location) throws NamingException {
        if (CALCULATORS.get(location.name()) == null) {
            VehicleSpeedCalculator calculator = (VehicleSpeedCalculator) CONTEXT.lookup("java:global/SUTMS/com.poojithairosha-ejb-1.0/VehicleSpeedCalculatorBean");
            CALCULATORS.put(location.name(), calculator);
            return calculator;
        }

        return CALCULATORS.get(location.name());
    }

    public Map<String, Object> getDataPagination(int page, String date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            List<Predicate> predicatesList = new ArrayList<>();
            List<Predicate> countPredicatesList = new ArrayList<>();

            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            JpaCriteriaQuery<DeviceData> query = criteriaBuilder.createQuery(DeviceData.class);
            JpaRoot<DeviceData> root = query.from(DeviceData.class);

            JpaCriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            JpaRoot<DeviceData> countRoot = countQuery.from(DeviceData.class);

            if(date != null) {
                LocalDate localDate = LocalDate.parse(date);
                Timestamp startOfDay = Timestamp.valueOf(localDate.atStartOfDay());
                Timestamp endOfDay = Timestamp.valueOf(localDate.atTime(LocalTime.MAX));

                predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("captureTime"), startOfDay));
                predicatesList.add(criteriaBuilder.lessThanOrEqualTo(root.get("captureTime"), endOfDay));

                countPredicatesList.add(criteriaBuilder.greaterThanOrEqualTo(countRoot.get("captureTime"), startOfDay));
                countPredicatesList.add(criteriaBuilder.lessThanOrEqualTo(countRoot.get("captureTime"), endOfDay));
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
                deviceDataList = session.createQuery(query.select(root))
                        .setFirstResult((page > 0 ? page - 1 : 0) * limit)
                        .setMaxResults(limit)
                        .getResultList();
            } else {
                query.select(root).where(criteriaBuilder.and(predicatesList.toArray(new Predicate[0])));
                deviceDataList = session.createQuery(query)
                        .setFirstResult((page > 0 ? page - 1 : 0) * limit)
                        .setMaxResults(limit)
                        .getResultList();
            }
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
