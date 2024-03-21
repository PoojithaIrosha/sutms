package com.poojithairosha.web.controller;

import com.google.gson.JsonObject;
import com.poojithairosha.core.model.DeviceData;
import com.poojithairosha.core.util.HibernateUtil;
import com.poojithairosha.web.service.VehicleDataService;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.persistence.Id;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.server.mvc.Viewable;
import org.hibernate.Session;

import javax.print.attribute.standard.Media;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public Viewable analysisReport(@DefaultValue("1") @QueryParam("page") int page, @Nullable @QueryParam("date") String date) {
        Map<String, Object> result = vehicleDataService.getDataPagination(page, date);
        System.out.println(result.get("deviceDataList"));

        return new Viewable("/frontend/analysis-report.jsp", result);
    }


}
