package com.aurea.und;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UndCreateTest {

    private static final Logger LOGGER = Logger.getLogger(UndCreateTest.class);

    @Value("${undCommand}")
    private String undCommand;
    private File executionFolder;

    private String currentFolder() throws IOException {
        return new File(".").getCanonicalPath();
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

    @Test
    public void generatesDbForThisPackage() throws Exception {
        String command = undCommand + " create -db myDb.udb -languages java add " + currentFolder() + "/src/test/java/com/aurea/und";
        LOGGER.info("Running process: " + command);
        Process process = Runtime.getRuntime().exec(command, new String[] {}, executionFolder);
        process.waitFor();

        assertThat(new File(executionFolder, "myDb.udb").exists()).isTrue();
    }
}
