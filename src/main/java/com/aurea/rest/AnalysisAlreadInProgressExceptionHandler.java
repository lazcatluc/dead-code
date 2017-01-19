package com.aurea.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class AnalysisAlreadInProgressExceptionHandler implements ExceptionMapper<AnalysisAlreadyInProgressException> {

    @Override
    public Response toResponse(AnalysisAlreadyInProgressException exception) {
        return Response.status(Status.CONFLICT).entity(exception.getJsonExceptionMessage())
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

}
