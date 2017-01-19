package com.aurea.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aurea.model.Project;
import com.aurea.model.ProjectStatus;
import com.aurea.repo.InitializedProjects;
import com.aurea.repo.ProjectCleaner;
import com.aurea.und.ProjectAnalyzer;

@Component
@Path("/projects")
public class ProjectEndpoint {
    @Autowired
    private InitializedProjects projectRepository;
    @Autowired
    private ProjectAnalyzer projectAnalyzer;
    @Autowired
    private ProjectCleaner projectCleaner;

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
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{projectId}")
    public RestProject getAllProjects(@PathParam("projectId") String projectId) {
        Project project = projectRepository.findOneInitialized(projectId);
        if (project == null) {
            throw new ProjectNotFoundException(projectId);
        }
        return new RestProject(project);
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{projectId}")
    public RestProject updateProject(@PathParam("projectId") String projectId) {
        Project project = projectRepository.findOneInitialized(projectId);
        if (project == null) {
            throw new ProjectNotFoundException(projectId);
        }
        if (project.getCurrentStatus() == ProjectStatus.PROCESSING) {
            throw new AnalysisAlreadyInProgressException(projectId);
        }
        Project updatedProject = projectAnalyzer.updateProject(project);
        return new RestProject(updatedProject);
    }
    
    @DELETE
    @Path("{projectId}")
    public Response deleteProject(@PathParam("projectId") String projectId) {
        projectCleaner.clean(projectId);
        return Response.ok().build();
    }
}
