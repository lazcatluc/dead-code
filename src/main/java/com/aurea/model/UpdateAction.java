package com.aurea.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class UpdateAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long actionId;
    @ManyToOne
    private Project project;
    private LocalDateTime actionTime = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private ProjectStatus currentStatus = ProjectStatus.PROCESSING;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Defect> defects = new ArrayList<>();
    private String failureReason;

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    public ProjectStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(ProjectStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<Defect> getDefects() {
        return Collections.unmodifiableList(defects);
    }

    public void setDefects(List<Defect> defects) {
        this.defects = new ArrayList<>(defects);
    }
    
    public void addDefect(Defect defect) {
        defects.add(defect);
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UpdateAction other = (UpdateAction) obj;
        if (actionId == null) {
            if (other.actionId != null)
                return false;
        } else if (!actionId.equals(other.actionId))
            return false;
        return true;
    }

}
