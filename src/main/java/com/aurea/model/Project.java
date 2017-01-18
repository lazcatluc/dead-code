package com.aurea.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long projectId;
    private String path;
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @OrderBy("actionId")
    private List<UpdateAction> updates = new ArrayList<>();

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        return "Project [projectId=" + projectId + ", path=" + path + ", getLastUpdate()=" + getLastUpdate() + "]";
    }

}
