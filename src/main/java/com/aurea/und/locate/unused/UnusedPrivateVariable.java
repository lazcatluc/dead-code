package com.aurea.und.locate.unused;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aurea.und.locate.PrivateVariable;
import com.scitools.understand.Entity;

@Service
public class UnusedPrivateVariable extends PrivateVariable {
    
    private static final Logger LOGGER = Logger.getLogger(UnusedPrivateVariable.class);

    @Override
    protected boolean matches(Entity entity) {
        LOGGER.debug("Trying entity " + entity.name());
        
        String[] ib = entity.ib(null);
        LOGGER.debug(Arrays.asList(ib));
        boolean reachedReferences = false;
        for (int i = 0; i < ib.length; i++) {
            String currentRow = ib[i].trim();
            if (reachedReferences && currentRow.startsWith("Use")) {
                LOGGER.debug("Found use");
                return false;        
            }
            if (currentRow.equals("References")) {
                reachedReferences = true;
            }
        }
        return true;
    }

}
