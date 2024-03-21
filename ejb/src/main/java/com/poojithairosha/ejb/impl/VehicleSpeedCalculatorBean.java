package com.poojithairosha.ejb.impl;

import com.poojithairosha.ejb.remote.VehicleSpeedCalculator;
import jakarta.ejb.Stateful;

@Stateful
public class VehicleSpeedCalculatorBean implements VehicleSpeedCalculator {

    private int sumOfSpeeds;
    private int vehicleCount;

    @Override
    public double calculateAverageSpeed(int vehicleSpeed) {
        sumOfSpeeds += vehicleSpeed;
        vehicleCount++;
        return (double) sumOfSpeeds / vehicleCount;
    }
}
