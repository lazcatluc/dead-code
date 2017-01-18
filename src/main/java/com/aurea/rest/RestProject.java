package com.aurea.rest;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.aurea.model.Defect;
import com.aurea.model.Project;
import com.aurea.model.ProjectStatus;
import com.aurea.model.UpdateAction;

@XmlRootElement(name = "project")
public class RestProject implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final Long projectId;
    private final ProjectStatus status;
    private final LocalDateTime lastUpdated;
    private final List<Defect> defects;
    
    public RestProject(Project project) {
        projectId = project.getProjectId();
        UpdateAction updateAction = project.getLastUpdate().get();
        status = updateAction.getCurrentStatus();
        lastUpdated = updateAction.getActionTime();
        defects = new ArrayList<>(updateAction.getDefects());
    }

    public Long getProjectId() {
        return projectId;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public List<Defect> getDefects() {
        return Collections.unmodifiableList(defects);
    }

}
