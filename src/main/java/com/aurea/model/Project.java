package com.aurea.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String projectId;
    private String path;
    private String url;
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @OrderBy("actionId")
    private List<UpdateAction> updates = new ArrayList<>();

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<UpdateAction> getUpdates() {
        return Collections.unmodifiableList(updates);
    }

    public void setUpdates(List<UpdateAction> updates) {
        this.updates = new ArrayList<>(updates);
    }

    public void addUpdate(UpdateAction updateAction) {
        updates.add(updateAction);
    }

    public Optional<UpdateAction> getLastUpdate() {
        if (updates.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(updates.get(updates.size() - 1));
    }
    
    public ProjectStatus getCurrentStatus() {
        return getLastUpdate().get().getCurrentStatus();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
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
        Project other = (Project) obj;
        if (projectId == null) {
            if (other.projectId != null)
                return false;
        } else if (!projectId.equals(other.projectId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Project [projectId=" + projectId + ", path=" + path + ", url=" + url + ", getLastUpdate()=" + getLastUpdate() + "]";
    }

    public LocalDateTime getAddedAt() {
        if (updates.isEmpty()) {
            return LocalDateTime.now();
        }
        return updates.get(0).getActionTime();
    }

}
