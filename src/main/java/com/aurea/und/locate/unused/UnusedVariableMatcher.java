package com.aurea.und.locate.unused;

import org.springframework.stereotype.Service;

import com.aurea.repo.ProjectEntity;

@Service
class UnusedVariableMatcher {
    boolean matches(ProjectEntity entity) {
        String[] ib = entity.ib(null);
        boolean reachedReferences = false;
        for (int i = 0; i < ib.length; i++) {
            String currentRow = ib[i].trim();
            if (reachedReferences && currentRow.startsWith("Use")) {
                return false;
            }
            if (currentRow.equals("References")) {
                reachedReferences = true;
            }
        }
        return true;
    }
}
