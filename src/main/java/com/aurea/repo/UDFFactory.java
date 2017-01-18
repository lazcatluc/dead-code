package com.aurea.repo;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class UDFFactory implements EntityRepoFactory {
    /* (non-Javadoc)
     * @see com.aurea.repo.EntityRepoFactory#newEntityRepo(java.lang.String)
     */
    @Override
    public EntityRepo newEntityRepo(String udbFile) throws IOException {
        return new UDBFileReader(udbFile);
    }
}
