package com.aurea.und.locate.unused;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aurea.repo.ProjectEntity;
import com.aurea.und.locate.PrivateMethod;

@Service
public class UnusedPrivateMethod extends PrivateMethod {

    private static final Logger LOGGER = Logger.getLogger(UnusedPrivateMethod.class);

    @Override
    protected boolean matches(ProjectEntity entity) {
        LOGGER.debug("Trying entity " + entity.name());

        String[] ib = entity.ib(null);
        boolean reachedCallBy = false;
        for (int i = 0; i < ib.length; i++) {
            if (reachedCallBy) {
                String caller = ib[i].trim();
                if (caller.equals("References")) {
                    return true;
                }
                if (!caller.endsWith(" recursive")) {
                    LOGGER.debug("Found usage " + caller);
                    return false;
                }
            }
            if (ib[i].contains("Called By")) {
                reachedCallBy = true;
            }
        }
        return true;
    }

}
