package com.aurea.repo;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;

public class UDBFileReader implements EntityRepo {
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
        return Arrays.stream(database.ents(entityLocator)).map(EntityWrapper::new);
    }

    @Override
    public void close() throws IOException {
        database.close();
    }
    
}
