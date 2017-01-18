package com.aurea.model;

public interface DeadCodeModelFactory {
    Project newProject();
    UpdateAction newUpdateAction();
    Defect newDefect();
}
