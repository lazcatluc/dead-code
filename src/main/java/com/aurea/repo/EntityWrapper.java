package com.aurea.repo;

import com.scitools.understand.Entity;
import com.scitools.understand.Reference;

public class EntityWrapper implements ProjectEntity {
    
    private final Entity entity;
    
    public EntityWrapper(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String[] ib(String descriptor) {
        return entity.ib(descriptor);
    }

    @Override
    public String name() {
        return entity.name();
    }

    @Override
    public String longname(boolean bool) {
        return entity.longname(bool);
    }

    @Override
    public Reference[] refs(String descriptor1, String descriptor2, boolean bool) {
        return entity.refs(descriptor1, descriptor2, bool);
    }

}
