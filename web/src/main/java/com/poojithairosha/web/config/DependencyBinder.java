package com.poojithairosha.web.config;

import com.poojithairosha.web.service.VehicleDataService;
import jakarta.ejb.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class DependencyBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(VehicleDataService.class).to(VehicleDataService.class).in(Singleton.class);
    }
}
