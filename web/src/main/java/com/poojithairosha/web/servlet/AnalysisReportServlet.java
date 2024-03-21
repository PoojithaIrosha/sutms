package com.poojithairosha.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.poojithairosha.core.model.DeviceData;
import com.poojithairosha.core.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@WebServlet(name = "AnalysisReportServlet", urlPatterns = "/analysis-report")
public class AnalysisReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<DeviceData> data = session.createQuery("from DeviceData", DeviceData.class).getResultList();
            List<JsonObject> jsonObjectList = new ArrayList<>();
            data.forEach(d -> {
                JsonObject json = new JsonObject();
                json.add("vehicleSpeed", gson.toJsonTree(d.getVehicleSpeed()));
                json.add("trafficLightStatus", gson.toJsonTree(d.getTrafficLightStatus()));

                JsonObject gpsJson = new JsonObject();
                gpsJson.add("latitude", gson.toJsonTree(d.getGpsCoordinates().getLatitude()));
                gpsJson.add("longitude", gson.toJsonTree(d.getGpsCoordinates().getLongitude()));
                gpsJson.add("location", gson.toJsonTree(d.getGpsCoordinates().getLocation()));

                json.add("gpsCoordinates", gpsJson);
                json.add("captureTime", gson.toJsonTree(d.getCaptureTime()));

                jsonObjectList.add(json);
            });

            resp.getWriter().write(gson.toJson(jsonObjectList));

        }
    }
}
