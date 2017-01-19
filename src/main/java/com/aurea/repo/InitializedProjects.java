package com.aurea.repo;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurea.model.Project;

@Service
@Transactional
public class InitializedProjects {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    public Project findOneInitialized(String id) {
        return initialize(projectRepository.findOne(id));
    }
    
    private Project initialize(Project findOne) {
        if (findOne == null) {
            return null;
        }
        findOne.getLastUpdate().ifPresent(updateAction -> updateAction.getDefects().size());
        return findOne;
    }
    
    public List<Project> findAllInitialized() {
        List<Project> projects = new ArrayList<>();
        projectRepository.findAll().forEach(project -> projects.add(initialize(project)));
        return projects;
    }
}
