package com.aurea.repo;

import java.io.Closeable;
import java.util.stream.Stream;

public interface EntityRepo extends Closeable {
    Stream<ProjectEntity> ents(String entityLocator);
}
