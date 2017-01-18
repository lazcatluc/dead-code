package com.aurea.und.locate.unused;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurea.repo.ProjectEntity;
import com.aurea.und.locate.Parameter;

@Service
public class UnusedParameter extends Parameter {
    @Autowired
    private UnusedVariableMatcher unusedVariableMatcher;
    
    @Override
    protected boolean matches(ProjectEntity entity) {
        return unusedVariableMatcher.matches(entity);
    }
}
