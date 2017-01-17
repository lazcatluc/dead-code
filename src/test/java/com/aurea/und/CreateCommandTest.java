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
public class CreateCommandTest extends UndTestFixture {

    @Autowired
    private CreateCommand createCommand;
    
    @Test
    public void generatesDbForThisPackage() throws Exception {
        Process process = createCommand.createUdb(executionFolder(), currentFolder()
                + "/src/und/java", "myDb.udb");
        process.waitFor();

        assertThat(new File(executionFolder(), "myDb.udb").exists()).isTrue();
    }
}
