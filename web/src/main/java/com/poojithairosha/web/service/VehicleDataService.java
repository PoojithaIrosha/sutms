package com.poojithairosha.web.service;

import com.poojithairosha.core.model.TrafficLightLocation;
import com.poojithairosha.core.util.DecimalFormatter;
import com.poojithairosha.ejb.remote.IoTDeviceData;
import com.poojithairosha.ejb.remote.TrafficPatternAnalyzer;
import com.poojithairosha.ejb.remote.VehicleSpeedCalculator;
import com.poojithairosha.web.dto.AvgSpeedDTO;
import jakarta.inject.Singleton;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> getDataPagination(int page, String date, String location) {
        try {
            IoTDeviceData deviceDataBean = (IoTDeviceData) CONTEXT.lookup("java:global/SUTMS/com.poojithairosha-ejb-1.0/IoTDeviceDataBean");
            return deviceDataBean.findDeviceDataPaginate(page, date, location);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<TrafficLightLocation, Map<Integer, Integer>> analyzeTrafficPatterns(String date) {
        try {
            TrafficPatternAnalyzer patternAnalyzer = (TrafficPatternAnalyzer) CONTEXT.lookup("java:global/SUTMS/com.poojithairosha-ejb-1.0/TrafficPatternAnalyzerBean");
            return patternAnalyzer.analyze(date);
        }catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

}
