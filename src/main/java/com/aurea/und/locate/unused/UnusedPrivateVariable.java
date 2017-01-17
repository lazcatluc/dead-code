package com.aurea.und.locate.unused;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurea.und.locate.PrivateVariable;
import com.scitools.understand.Entity;

@Service
public class UnusedPrivateVariable extends PrivateVariable {
    
    @Autowired
    private UnusedVariableMatcher unusedVariableMatcher;
    
    @Override
    protected boolean matches(Entity entity) {
        return unusedVariableMatcher.matches(entity);
    }

}
