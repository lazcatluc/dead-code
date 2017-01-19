package com.aurea.repo;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;

public class UDBFileReader implements EntityRepo {
    
    private static final Logger LOGGER = Logger.getLogger(UDBFileReader.class);
    
    private final com.scitools.understand.Database database;

    public UDBFileReader(String udbFilePath) throws IOException {
        try {
            this.database = Understand.open(udbFilePath);
        } catch (UnderstandException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Stream<ProjectEntity> ents(String entityLocator) {
        LOGGER.info("Getting entities: " + entityLocator);
        Stream<ProjectEntity> entities = Arrays.stream(database.ents(entityLocator)).map(EntityWrapper::new);
        LOGGER.info("Got entities: " + entityLocator);
        return entities;
    }

    @Override
    public void close() throws IOException {
        database.close();
    }
    
}
