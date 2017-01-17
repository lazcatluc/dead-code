package com.aurea.und.locate.unused;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurea.und.locate.Parameter;
import com.scitools.understand.Entity;

@Service
public class UnusedParameter extends Parameter {
    @Autowired
    private UnusedVariableMatcher unusedVariableMatcher;
    
    @Override
    protected boolean matches(Entity entity) {
        return unusedVariableMatcher.matches(entity);
    }
}
