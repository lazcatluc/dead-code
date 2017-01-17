package com.aurea.und;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Understand;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryDbTest {
    @Autowired
    private CreateCommand createCommand;
    @Autowired
    private AnalyzeCommand analyzeCommand;
    private Database database;
    @Value("${undCommand}")
    private String undCommand;
    @Value("${workFolder}")
    private String currentFolder;
    private File executionFolder;

    @Before
    public void setUp() throws Exception {
        executionFolder = new File(currentFolder() + "/src/test/" + UUID.randomUUID());
        executionFolder.mkdirs();
        createCommand.createUdb(executionFolder(), currentFolder() + "/src/und/java", "myDb.udb").waitFor();
        File udbFileToAnalyze = new File(executionFolder(), "myDb.udb");
        analyzeCommand.analyze(executionFolder(), udbFileToAnalyze).waitFor();

        database = Understand.open(udbFileToAnalyze.getAbsolutePath());
    }

    @After
    public void cleanUp() throws Exception {
        database.close();
        if (executionFolder.exists()) {
            for (File file : executionFolder.listFiles()) {
                file.delete();
            }
        }
        executionFolder.delete();
    }

    @Test
    public void canQueryCreatedDatabase() throws Exception {
        assertThat(Arrays.stream(database.ents("package")).map(Entity::name).anyMatch(name -> name.equals("und")))
                .isTrue();
        assertThat(Arrays.stream(database.ents("class")).map(Entity::name).anyMatch(name -> name.equals("und.MyClass")))
                .isTrue();
        assertThat(Arrays.stream(database.ents("method")).map(Entity::name).anyMatch(name -> name.equals("MyClass.undMethod1")))
                .isTrue();        
        assertThat(Arrays.stream(database.ents("variable")).map(Entity::name).anyMatch(name -> name.equals("MyClass.undSomeField")))
                .isTrue();
        assertThat(Arrays.stream(database.ents("parameter")).map(entity -> entity.longname(true))
                .anyMatch(name -> name.equals("und.MyClass.undMethod1.someMethodParameter"))).isTrue();
        
    }

    protected String currentFolder() throws IOException {
        return new File(currentFolder).getCanonicalPath();
    }

    protected File executionFolder() {
        return executionFolder;
    }

}
