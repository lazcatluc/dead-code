package com.aurea;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.aurea.rest.ProjectEndpoint;
import com.aurea.rest.ProjectNotFoundExceptionHandler;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(ProjectEndpoint.class);
        register(ProjectNotFoundExceptionHandler.class);
    }

}
