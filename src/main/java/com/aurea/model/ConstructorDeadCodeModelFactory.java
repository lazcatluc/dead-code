package com.aurea.model;

import org.springframework.stereotype.Component;

@Component
public class ConstructorDeadCodeModelFactory implements DeadCodeModelFactory {

    @Override
    public Project newProject() {
        return new Project();
    }

    @Override
    public UpdateAction newUpdateAction() {
        return new UpdateAction();
    }

    @Override
    public Defect newDefect() {
        return new Defect();
    }

}
