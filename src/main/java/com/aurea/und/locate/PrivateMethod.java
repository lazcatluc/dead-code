package com.aurea.und.locate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;

@Service
public class PrivateMethod {
    
    public List<Entity> getEntities(Database database) {
        return Arrays.stream(database.ents("private method")).filter(this::isOwnEntity).collect(Collectors.toList());
    }
    
    protected boolean isOwnEntity(Entity entity) {
        String name = entity.longname(true);
        return !name.startsWith("java.") && !name.startsWith("javax.") && !name.startsWith("sun.");
    }
    
}
