package com.poojithairosha.web.controller;

import com.poojithairosha.core.model.TrafficLightLocation;
import com.poojithairosha.web.service.VehicleDataService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/vehicle-data")
public class VehicleDataController {

    @Inject
    private VehicleDataService vehicleDataService;

    @GET
    @Path("/speed/{location}/{speed}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVehicleSpeed(@PathParam("location") String location, @PathParam("speed") int speed) {
        return Response.ok(vehicleDataService.getAvgVehicleSpeed(TrafficLightLocation.valueOf(location), speed)).build();
    }

}
