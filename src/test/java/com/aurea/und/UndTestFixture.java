package com.aurea.und;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;

public class UndTestFixture {

    @Value("${undCommand}")
    private String undCommand;
    @Value("${workFolder}")
    private String currentFolder;
    private File executionFolder;

    protected String currentFolder() throws IOException {
        return new File(currentFolder).getCanonicalPath();
    }
    
    protected File executionFolder() {
        return executionFolder;
    }

    @Before
    public void setUp() throws Exception {
        executionFolder = new File(currentFolder() + "/src/test/" + UUID.randomUUID());
        executionFolder.mkdirs();
    }

    @After
    public void cleanUp() throws Exception {
        if (executionFolder.exists()) {
            for (File file : executionFolder.listFiles()) {
                file.delete();
            }
        }
        executionFolder.delete();
    }

}