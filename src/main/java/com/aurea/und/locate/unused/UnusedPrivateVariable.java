package com.aurea.und.locate.unused;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurea.repo.ProjectEntity;
import com.aurea.und.locate.PrivateVariable;

@Service
public class UnusedPrivateVariable extends PrivateVariable {
    
    @Autowired
    private UnusedVariableMatcher unusedVariableMatcher;
    
    @Override
    protected boolean matches(ProjectEntity entity) {
        return unusedVariableMatcher.matches(entity);
    }

}
