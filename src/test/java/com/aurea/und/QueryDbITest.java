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

import com.aurea.repo.EntityRepo;
import com.aurea.repo.EntityRepoFactory;
import com.aurea.repo.ProjectEntity;
import com.aurea.und.locate.Parameter;
import com.aurea.und.locate.PrivateMethod;
import com.aurea.und.locate.PrivateVariable;
import com.aurea.und.locate.unused.UnusedParameter;
import com.aurea.und.locate.unused.UnusedPrivateMethod;
import com.aurea.und.locate.unused.UnusedPrivateVariable;
import com.scitools.understand.Reference;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryDbITest {

    private static final Logger LOGGER = Logger.getLogger(QueryDbITest.class);

    @Autowired
    private CreateCommand createCommand;
    @Autowired
    private AnalyzeCommand analyzeCommand;
    @Autowired
    private PrivateMethod privateMethod;
    @Autowired
    private UnusedPrivateMethod unusedPrivateMethod;
    @Autowired
    private PrivateVariable privateVariable;
    @Autowired
    private UnusedPrivateVariable unusedPrivateVariable;
    @Autowired
    private UnusedParameter unusedParameter;
    @Autowired
    private Parameter parameter;
    @Autowired
    private EntityRepoFactory repoFactory;
    private EntityRepo entityRepo;
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

        entityRepo = repoFactory.newEntityRepo(udbFileToAnalyze.getAbsolutePath());
    }

    @After
    public void cleanUp() throws Exception {
        entityRepo.close();
        if (executionFolder.exists()) {
            for (File file : executionFolder.listFiles()) {
                file.delete();
            }
        }
        executionFolder.delete();
    }

    @Test
    public void canQueryCreatedDatabase() throws Exception {
        assertThat(entityRepo.ents("package").map(ProjectEntity::name).anyMatch(name -> name.equals("und")))
                .isTrue();
        Optional<ProjectEntity> maybeClass = entityRepo.ents("class")
                .filter(entity -> entity.name().equals("und.MyClass")).findAny();
        assertThat(maybeClass.isPresent()).isTrue();
        logReferences(maybeClass.get());
        Optional<ProjectEntity> maybeMethod = entityRepo.ents("private method")
                .filter(entity -> entity.longname(true).equals("und.MyClass.unusedStaticMethod")).findAny();
        assertThat(maybeMethod.isPresent()).isTrue();
        
        logReferences(maybeMethod.get());
        LOGGER.info(Arrays.asList(maybeMethod.get().ib(null)));
        Optional<ProjectEntity> maybeVariable = entityRepo.ents("variable").filter(
                entity -> entity.name().equals("MyClass.undSomeField")).findFirst();
        assertThat(maybeVariable.isPresent()).isTrue();
        LOGGER.info(Arrays.asList(maybeVariable.get().ib(null)));
        
        Optional<ProjectEntity> maybeParameter = entityRepo.ents("parameter").filter(
                entity -> entity.longname(true).equals("und.MyClass.defaultMethod.someParameter")).findAny();
        assertThat(maybeParameter.isPresent()).isTrue();
        LOGGER.info(Arrays.asList(maybeParameter.get().ib(null)));
        
        assertThat(privateMethod.getEntities(entityRepo).size()).isEqualTo(7);
        assertThat(privateVariable.getEntities(entityRepo).size()).isEqualTo(3);
        assertThat(parameter.getEntities(entityRepo).size()).isEqualTo(2);
        assertThat(unusedPrivateMethod.getEntities(entityRepo).size()).isEqualTo(2);
        assertThat(unusedPrivateVariable.getEntities(entityRepo).size()).isEqualTo(2);
        assertThat(unusedParameter.getEntities(entityRepo).size()).isEqualTo(1);
    }

    private void logReferences(ProjectEntity method) {
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
