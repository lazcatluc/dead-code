package com.aurea.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ProjectNotFoundExceptionHandler implements ExceptionMapper<ProjectNotFoundException> {

    @Override
    public Response toResponse(ProjectNotFoundException exception) {
        return Response.status(Status.NOT_FOUND).entity(exception.getJsonExceptionMessage())
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

}
