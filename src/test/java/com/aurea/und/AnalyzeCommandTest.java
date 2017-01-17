package com.aurea.und;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnalyzeCommandTest extends UndTestFixture {

    @Autowired
    private CreateCommand createCommand;

    @Autowired
    private AnalyzeCommand analyzeCommand;

    @Test
    public void canAnalyzeCreatedProject() throws Exception {
        Process createProcess = createCommand.createUdb(executionFolder(),
                currentFolder() + "/src/und/java", "myDb.udb");
        createProcess.waitFor();

        Process analyzeProcess = analyzeCommand.analyze(executionFolder(), new File(executionFolder(), "myDb.udb"));
        analyzeProcess.waitFor();
        
        assertThat(analyzeProcess.exitValue()).isEqualTo(0);
    }
}
