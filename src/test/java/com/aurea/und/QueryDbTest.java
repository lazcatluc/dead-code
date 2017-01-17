package com.aurea.und;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Understand;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryDbTest extends UndTestFixture {
    @Autowired
    private CreateCommand createCommand;
    @Autowired
    private AnalyzeCommand analyzeCommand;
    private Database database;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        createCommand.createUdb(executionFolder(), currentFolder() + "/src/und/java", "myDb.udb").waitFor();
        File udbFileToAnalyze = new File(executionFolder(), "myDb.udb");
        analyzeCommand.analyze(executionFolder(), udbFileToAnalyze).waitFor();

        database = Understand.open(udbFileToAnalyze.getAbsolutePath());
    }

    @After
    @Override
    public void cleanUp() throws Exception {
        database.close();
        super.cleanUp();
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
}
