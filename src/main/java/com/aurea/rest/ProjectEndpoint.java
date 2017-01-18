package com.aurea.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aurea.repo.InitializedProjects;
import com.aurea.und.ProjectAnalyzer;

@Component
@Path("/projects")
public class ProjectEndpoint {
    @Autowired
    private InitializedProjects projectRepository;
    @Autowired
    private ProjectAnalyzer projectAnalyzer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestProject> getAllProjects() {
        return projectRepository.findAllInitialized().stream().map(RestProject::new).collect(Collectors.toList());
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestProject addProject(ProjectUrl projectUrl) {
        return new RestProject(projectAnalyzer.addProject(projectUrl.getUrl()));
    }
}
