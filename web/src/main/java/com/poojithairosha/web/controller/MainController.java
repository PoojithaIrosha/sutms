package com.poojithairosha.web.controller;

import com.google.gson.JsonObject;
import com.poojithairosha.core.model.DeviceData;
import com.poojithairosha.core.model.GPSCoordinates;
import com.poojithairosha.core.model.TrafficLightLocation;
import com.poojithairosha.core.util.HibernateUtil;
import com.poojithairosha.web.service.VehicleDataService;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.persistence.Id;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.mvc.Viewable;
import org.hibernate.Session;

import javax.print.attribute.standard.Media;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Path("/")
public class MainController {

    @Inject
    private VehicleDataService vehicleDataService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable home() {
        return new Viewable("/frontend/home.jsp");
    }

    @GET
    @Path("/analysis-report")
    @Produces(MediaType.TEXT_HTML)
    public Viewable analysisReport(@DefaultValue("1") @QueryParam("page") int page, @Nullable @QueryParam("date") String date, @Nullable @QueryParam("location") String location) {
        Map<String, Object> result = vehicleDataService.getDataPagination(page, date, location);
        return new Viewable("/frontend/analysis-report.jsp", result);
    }

    @GET
    @Path("/vehicle-data/speed/{location}/{speed}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVehicleSpeed(@PathParam("location") String location, @PathParam("speed") int speed) {
        return Response.ok(vehicleDataService.getAvgVehicleSpeed(TrafficLightLocation.valueOf(location), speed)).build();
    }

    @GET
    @Path("/traffic-patterns/{date}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable trafficPatterns(@PathParam("date") String date) {
        Map<TrafficLightLocation, Map<Integer, Integer>> analyzedTrafficPatterns = vehicleDataService.analyzeTrafficPatterns(date);
        return new Viewable("/frontend/traffic-patterns.jsp", analyzedTrafficPatterns);
    }

//    @GET
//    @Path("/busy-hours/{date}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response test(@PathParam("date") String date) {
//        return Response.ok().build();
//    }

}
