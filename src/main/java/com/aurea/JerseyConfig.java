package com.aurea;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.aurea.rest.AnalysisAlreadInProgressExceptionHandler;
import com.aurea.rest.ProjectEndpoint;
import com.aurea.rest.ProjectNotFoundExceptionHandler;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(ProjectEndpoint.class);
        register(ProjectNotFoundExceptionHandler.class);
        register(AnalysisAlreadInProgressExceptionHandler.class);
        configureSwagger();
    }

    private void configureSwagger() {
        // Available at localhost:port/swagger.json
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);

        BeanConfig config = new BeanConfig();
        config.setConfigId("swagger-dead-code");
        config.setTitle("Swagger for Dead Code");
        config.setVersion("v1");
        config.setContact("Catalin Lazar <catalin.lazar@aurea.com>");
        config.setSchemes(new String[] { "http", "https" });
        config.setBasePath("api");
        config.setResourcePackage(ProjectEndpoint.class.getPackage().getName());
        config.setPrettyPrint(true);
        config.setScan(true);
    }
}
