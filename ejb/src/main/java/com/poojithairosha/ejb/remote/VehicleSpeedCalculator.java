package com.poojithairosha.ejb.remote;

import jakarta.ejb.Remote;

@Remote
public interface VehicleSpeedCalculator {

    double calculateAverageSpeed(int vehicleSpeed);

}
