package com.aurea.und.locate;

import java.util.List;
import java.util.stream.Collectors;

import com.aurea.repo.EntityRepo;
import com.aurea.repo.ProjectEntity;

public abstract class EntityLocator {

    protected abstract String entityLocator();

    public List<ProjectEntity> getEntities(EntityRepo entityRepo) {
        return entityRepo.ents(entityLocator()).filter(this::isOwnEntity).filter(this::matches)
                .collect(Collectors.toList());
    }

    protected boolean isOwnEntity(ProjectEntity entity) {
        String name = entity.longname(true);
        return !name.startsWith("java.") && !name.startsWith("javax.") && !name.startsWith("sun.");
    }

    protected boolean matches(ProjectEntity entity) {
        return true;
    }
}