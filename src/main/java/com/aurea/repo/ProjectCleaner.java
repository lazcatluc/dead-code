package com.aurea.repo;

import java.io.File;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aurea.model.Project;
import com.aurea.rest.ProjectNotFoundException;

@Service
@Transactional
public class ProjectCleaner {
    
    private static final Logger LOGGER = Logger.getLogger(ProjectCleaner.class);
    
    @Value("${workFolder}")
    private String workFolder;

    @Autowired
    private ProjectRepository projectRepository;
    
    public void clean(String projectId) {
        Project project = projectRepository.findOne(projectId);
        if (project == null) {
            throw new ProjectNotFoundException(projectId);
        }
        LOGGER.info("Deleting "+project);
        projectRepository.delete(project);
        removeLocalRepository(project);
        removeUdbFile(project);
    }

    private void removeUdbFile(Project project) {
        File udbFile = new File(workFolder, project.getProjectId() + ".udb");
        LOGGER.info("Removing database file: "+udbFile.getAbsolutePath());
        Cleaner.cleanUpRecursively(udbFile);
    }

    private void removeLocalRepository(Project project) {
        LOGGER.info("Removing local project folder: "+project.getPath());
        Cleaner.cleanUpRecursively(new File(project.getPath()));
    }

}
