package com.aurea.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aurea.repo.InitializedProjects;

@Component
@Path("/projects")
public class ProjectEndpoint {
    @Autowired
    private InitializedProjects projectRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestProject> getAllProjects() {
        return projectRepository.findAllInitialized().stream().map(RestProject::new).collect(Collectors.toList());
    }
}
