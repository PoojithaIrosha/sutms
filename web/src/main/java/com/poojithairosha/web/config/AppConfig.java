package com.poojithairosha.web.config;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

public class AppConfig extends ResourceConfig {

    public AppConfig() {
        packages("com.poojithairosha.web.controller");
        packages("com.poojithairosha.web.service");
        register(MultiPartFeature.class);
        register(JspMvcFeature.class);
        register(DependencyBinder.class);
        property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/views");
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }
}
