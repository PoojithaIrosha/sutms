package com.poojithairosha.ejb.message;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.poojithairosha.ejb.remote.IoTDeviceData;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.*;

import java.util.Date;
import java.util.Map;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "UTMSQueue")
        }
)
public class TrafficDataReceiverMDB implements MessageListener {

    @EJB(lookup = "java:global/SUTMS/com.poojithairosha-ejb-1.0/IoTDeviceDataBean")
    private IoTDeviceData ioTDeviceData;

    private WsClient client = new WsClient();
    private Gson gson = new Gson();

    @Override
    public void onMessage(Message message) {
        try {
            Map<String, Object> data = message.getBody(Map.class);

            int vehicleSpeed = (int) data.get("vehicleSpeed");
            String trafficLightStatus = (String) data.get("trafficLightStatus");
            Map<String, Double> coordinates = (Map) data.get("gpsCoordinates");
            Date captureTime = new Date((long) data.get("time"));

            JsonObject json = new JsonObject();
            json.add("vehicleSpeed", gson.toJsonTree(vehicleSpeed));
            json.add("trafficLightStatus", gson.toJsonTree(trafficLightStatus));

            JsonObject gpsJson = new JsonObject();
            gpsJson.add("latitude", gson.toJsonTree(coordinates.get("latitude")));
            gpsJson.add("longitude", gson.toJsonTree(coordinates.get("longitude")));
            gpsJson.add("location", gson.toJsonTree(coordinates.get("location")));

            json.add("gpsCoordinates", gpsJson);
            json.add("captureTime", gson.toJsonTree(captureTime));

            client.sendMessage(json.toString());

            ioTDeviceData.saveData(vehicleSpeed, trafficLightStatus, coordinates, captureTime);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
