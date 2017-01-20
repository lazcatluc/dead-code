package com.aurea.rest;

import java.io.Serializable;
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
    
    private String projectId;
    private String url;
    private String addedAt;
    private ProjectStatus status;
    private String lastUpdated;
    private List<Defect> defects;
    
    public RestProject() {
        
    }
    
    public static RestProject fromProject(Project project) {
        if (project.getCurrentStatus() == ProjectStatus.FAILED) {
            return new RestProjectWithReason(project);
        }
        return new RestProject(project);
    }
    
    private RestProject(Project project) {
        projectId = project.getProjectId();
        url = project.getUrl();
        addedAt = project.getAddedAt().toString();
        UpdateAction updateAction = project.getLastUpdate().get();
        status = updateAction.getCurrentStatus();
        lastUpdated = updateAction.getActionTime().toString();
        defects = new ArrayList<>(updateAction.getDefects());
    }

    public String getUrl() {
        return url;
    }
    
    public String getProjectId() {
        return projectId;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
    
    public String getAddedAt() {
        return addedAt;
    }

    public List<Defect> getDefects() {
        return Collections.unmodifiableList(defects);
    }

    @Override
    public String toString() {
        return "RestProject [projectId=" + projectId + ", status=" + status + ", lastUpdated=" + lastUpdated
                + ", defects=" + defects + "]";
    }
    
    @XmlRootElement(name = "project")
    public static class RestProjectWithReason extends RestProject {
        private static final long serialVersionUID = 8887043527651873602L;
        private String reason;

        private RestProjectWithReason(Project project) {
            super(project);
            this.reason = project.getLastUpdate().get().getFailureReason();
        }
        
        public String getReason() {
            return reason;
        }
    }

}
