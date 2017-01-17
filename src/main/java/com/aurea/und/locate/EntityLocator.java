package com.aurea.und.locate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;

public abstract class EntityLocator {

    public EntityLocator() {
        super();
    }

    protected abstract String entityLocator();

    public List<Entity> getEntities(Database database) {
        return Arrays.stream(database.ents(entityLocator())).filter(this::isOwnEntity).collect(Collectors.toList());
    }

    protected boolean isOwnEntity(Entity entity) {
        String name = entity.longname(true);
        return !name.startsWith("java.") && !name.startsWith("javax.") && !name.startsWith("sun.");
    }

}