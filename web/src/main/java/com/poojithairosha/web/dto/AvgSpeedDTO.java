package com.poojithairosha.web.dto;

public class AvgSpeedDTO {

    private double avgSpeed;

    public AvgSpeedDTO() {
    }

    public AvgSpeedDTO(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    @Override
    public String toString() {
        return "AvgSpeedDTO{" +
                "avgSpeed=" + avgSpeed +
                '}';
    }

}
