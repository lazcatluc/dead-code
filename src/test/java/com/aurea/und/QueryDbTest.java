package com.aurea.und;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aurea.und.locate.PrivateMethod;
import com.aurea.und.locate.Parameter;
import com.aurea.und.locate.PrivateVariable;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import com.scitools.understand.Understand;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryDbTest {

    private static final Logger LOGGER = Logger.getLogger(QueryDbTest.class);

    @Autowired
    private CreateCommand createCommand;
    @Autowired
    private AnalyzeCommand analyzeCommand;
    @Autowired
    private PrivateMethod privateMethod;
    @Autowired
    private PrivateVariable privateVariable;
    @Autowired
    private Parameter parameter;
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
        Optional<Entity> maybeClass = Arrays.stream(database.ents("class"))
                .filter(entity -> entity.name().equals("und.MyClass")).findAny();
        assertThat(maybeClass.isPresent()).isTrue();
        logReferences(maybeClass.get());
        Optional<Entity> maybeMethod = Arrays.stream(database.ents("private method"))
                .filter(entity -> entity.name().equals("MyClass.undMethod1")).findAny();
        assertThat(maybeMethod.isPresent()).isTrue();
        logReferences(maybeMethod.get());
        assertThat(Arrays.stream(database.ents("variable")).map(Entity::name)
                .anyMatch(name -> name.equals("MyClass.undSomeField"))).isTrue();
        assertThat(Arrays.stream(database.ents("parameter")).map(entity -> entity.longname(true))
                .anyMatch(name -> name.equals("und.MyClass.undMethod1.someMethodParameter"))).isTrue();
        assertThat(privateMethod.getEntities(database).size()).isEqualTo(3);
        assertThat(privateVariable.getEntities(database).size()).isEqualTo(1);
        assertThat(parameter.getEntities(database).size()).isEqualTo(2);
    }

    private void logReferences(Entity method) {
        Reference[] refs = method.refs(null, null, false);
        LOGGER.info("Getting references for method: " + refs.length);
        Arrays.stream(refs)
                .forEach(ref -> LOGGER.info("entity referenced: " + ref.ent().longname(true) + "; " + ref.ent().id()
                        + "; " + ref.ent().kind() + "; " + ref.file().name() + ":" + ref.line() + ":" + ref.column()
                        + "; entity referring=" + ref.scope().longname(true)));
    }

    protected String currentFolder() throws IOException {
        return new File(currentFolder).getCanonicalPath();
    }

    protected File executionFolder() {
        return executionFolder;
    }

}
