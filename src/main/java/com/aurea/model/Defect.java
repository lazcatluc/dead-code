package com.aurea.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Defect implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long defectId;
    @Enumerated(EnumType.STRING)
    private DefectType defectType;
    private String projectFile;
    private int defectLine;
    private int defectColumn;
    private String entityName;
    @JsonIgnore
    private UpdateAction updateAction;

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public DefectType getDefectType() {
        return defectType;
    }

    public void setDefectType(DefectType defectType) {
        this.defectType = defectType;
    }

    public String getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(String projectFile) {
        this.projectFile = projectFile;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getDefectLine() {
        return defectLine;
    }

    public void setDefectLine(int defectLine) {
        this.defectLine = defectLine;
    }

    public int getDefectColumn() {
        return defectColumn;
    }

    public void setDefectColumn(int defectColumn) {
        this.defectColumn = defectColumn;
    }

    public UpdateAction getUpdateAction() {
        return updateAction;
    }

    public void setUpdateAction(UpdateAction updateAction) {
        this.updateAction = updateAction;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((defectId == null) ? 0 : defectId.hashCode());
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
        Defect other = (Defect) obj;
        if (defectId == null) {
            if (other.defectId != null)
                return false;
        } else if (!defectId.equals(other.defectId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Defect [defectId=" + defectId + ", defectType=" + defectType + ", projectFile=" + projectFile
                + ", defectLine=" + defectLine + ", defectColumn=" + defectColumn + ", entityName=" + entityName + "]";
    }

}
