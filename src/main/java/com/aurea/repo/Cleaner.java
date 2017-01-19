package com.aurea.repo;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class Cleaner {
    
    private static final Logger LOGGER = Logger.getLogger(Cleaner.class);
    
    private Cleaner() {
        
    }
    
    public static void cleanUpRecursively(File currentFile) {
        LOGGER.debug("Cleaning up "+currentFile);
        if (currentFile == null || !currentFile.exists()) {
            return;
        }
        if (currentFile.isDirectory()) {
            Arrays.stream(currentFile.listFiles()).forEach(Cleaner::cleanUpRecursively);
        }
        currentFile.delete();
    }
}
