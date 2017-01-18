package com.aurea.repo;

import java.io.IOException;

@FunctionalInterface
public interface EntityRepoFactory {

    EntityRepo newEntityRepo(String udbFile) throws IOException;

}