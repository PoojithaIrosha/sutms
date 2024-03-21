package com.poojithairosha.ejb.remote;

import com.poojithairosha.core.model.TrafficLightLocation;
import jakarta.ejb.Remote;

import java.util.Map;

@Remote
public interface TrafficPatternAnalyzer {

    Map<TrafficLightLocation, Map<Integer, Integer>> analyze(String date);

}
