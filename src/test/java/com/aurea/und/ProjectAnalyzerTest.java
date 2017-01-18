package com.aurea.und;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aurea.model.DeadCodeModelFactory;
import com.aurea.model.Defect;
import com.aurea.model.DefectType;
import com.aurea.model.Project;
import com.aurea.model.ProjectStatus;
import com.aurea.model.UpdateAction;
import com.aurea.repo.EntityRepo;
import com.aurea.repo.EntityRepoFactory;
import com.aurea.repo.ProjectEntity;
import com.aurea.repo.ProjectRepository;
import com.aurea.repo.RepoBrowser;
import com.aurea.und.locate.unused.UnusedParameter;
import com.aurea.und.locate.unused.UnusedPrivateMethod;
import com.aurea.und.locate.unused.UnusedPrivateVariable;
import com.scitools.understand.Reference;

public class ProjectAnalyzerTest {

    @InjectMocks
    private ProjectAnalyzer projectAnalyzer;
    @Mock
    private RepoBrowser repoBrowser;
    @Mock
    private ProjectRepository projectRepository;
    @Mock(answer = Answers.RETURNS_MOCKS)
    private CreateCommand createCommand;
    @Mock(answer = Answers.RETURNS_MOCKS)
    private AnalyzeCommand analyzeCommand;
    @Mock(answer = Answers.RETURNS_MOCKS)
    private EntityRepo entityRepo;
    @Mock
    private UnusedPrivateMethod unusedPrivateMethod;
    @Mock
    private UnusedPrivateVariable unusedPrivateVariable;
    @Mock
    private UnusedParameter unusedParameter;    

    private Project project;
    private UpdateAction updateAction;
    private Defect defect;

    private ThreadPoolTaskExecutor executor;
    @Mock(answer = Answers.RETURNS_MOCKS)
    private Reference reference;

    @Before
    public void setUp() throws Exception {
        projectAnalyzer = new ProjectAnalyzer();
        MockitoAnnotations.initMocks(this);
        executor = new ThreadPoolTaskExecutor();
        executor.afterPropertiesSet();
        projectAnalyzer.setExecutor(executor);
        project = new Project();
        updateAction = new UpdateAction();
        defect = new Defect();
        projectAnalyzer.setModelFactory(new DeadCodeModelFactory() {
            @Override
            public Project newProject() {
                return project;
            }

            @Override
            public UpdateAction newUpdateAction() {
                return updateAction;
            }

            @Override
            public Defect newDefect() {
                return defect;
            }
        });
        projectAnalyzer.setEntityRepoFactory(new EntityRepoFactory() {
            @Override
            public EntityRepo newEntityRepo(String udbFile) {
                return entityRepo;
            }
        });
        when(entityRepo.ents(any(String.class))).thenReturn(Stream.empty());
        projectAnalyzer.setWorkFolder(new File("/tmp"));
        when(projectRepository.save(project)).thenReturn(project);
        repoBrowserFile();
    }

    @Test
    public void addingANewProjectCallsRepository() {
        projectAnalyzer.addProject("/some/git/repo");

        verify(projectRepository, atLeastOnce()).save(project);
    }

    @Test
    public void addingANewProjectAddsAnUpdateActionInProgress() {
        Project project = projectAnalyzer.addProject("/some/git/repo");

        assertThat(project.getLastUpdate().get().getCurrentStatus()).isEqualTo(ProjectStatus.PROCESSING);
    }

    @Test
    public void addingANewProjectWillUpdateRepoFromRepoBrowser() throws Exception {
        File projectFile = repoBrowserFile();

        addSomeGitRepoAwatingTermination();

        assertThat(project.getPath()).isEqualTo(projectFile.getCanonicalPath());
    }

    @Test
    public void addingANewProjectWillSaveItTwice() throws Exception {
        addSomeGitRepoAwatingTermination();

        verify(projectRepository, atLeast(2)).save(project);
    }

    @Test
    public void whenGitFailsProjectGoesToFailedStatus() throws Exception {
        when(repoBrowser.downloadProject(any(String.class))).thenThrow(mock(GitAPIException.class));

        addSomeGitRepoAwatingTermination();

        assertThat(project.getLastUpdate().get().getCurrentStatus()).isEqualTo(ProjectStatus.FAILED);
    }

    @Test
    public void whenGitFailsProjectLogsReason() throws Exception {
        GitAPIException exception = mock(GitAPIException.class);
        when(exception.getMessage()).thenReturn("some-exception");
        when(repoBrowser.downloadProject(any(String.class))).thenThrow(exception);

        addSomeGitRepoAwatingTermination();

        assertThat(project.getLastUpdate().get().getFailureReason()).isEqualTo("some-exception");
    }

    @Test
    public void addingANewProjectWillRunCreateCommand() throws Exception {
        addSomeGitRepoAwatingTermination();

        verify(createCommand, times(1)).createUdb(any(File.class), eq(project.getPath()), any(String.class));
    }

    @Test
    public void failingCreateCommandWillCauseProjectToBeInFailedStatus() throws Exception {
        when(createCommand.createUdb(any(File.class), any(String.class), any(String.class)))
                .thenThrow(new IOException("some-exception"));

        addSomeGitRepoAwatingTermination();

        assertThat(project.getLastUpdate().get().getCurrentStatus()).isEqualTo(ProjectStatus.FAILED);
    }

    @Test
    public void nonZeroTerminationCauseAtCreateUDBProjectToBeInFailedState() throws Exception {
        Process process = mock(Process.class);
        when(createCommand.createUdb(any(File.class), any(String.class), any(String.class))).thenReturn(process);
        when(process.waitFor()).thenReturn(1);

        addSomeGitRepoAwatingTermination();

        assertThat(project.getLastUpdate().get().getCurrentStatus()).isEqualTo(ProjectStatus.FAILED);
    }
    
    @Test
    public void addingANewProjectWillRunAnalyzeCommand() throws Exception {
        addSomeGitRepoAwatingTermination();

        verify(analyzeCommand, times(1)).analyze(any(File.class), any(File.class));
    }  
    
    @Test
    public void failingAnalyzeCommandWillCauseProjectToBeInFailedStatus() throws Exception {
        when(analyzeCommand.analyze(any(File.class), any(File.class)))
                .thenThrow(new IOException("some-exception"));

        addSomeGitRepoAwatingTermination();

        assertThat(project.getLastUpdate().get().getCurrentStatus()).isEqualTo(ProjectStatus.FAILED);
    }
    
    @Test
    public void nonZeroTerminationAtAnalyzeUDBCausesProjectToBeInFailedState() throws Exception {
        Process process = mock(Process.class);
        when(analyzeCommand.analyze(any(File.class), any(File.class))).thenReturn(process);
        when(process.waitFor()).thenReturn(-1);

        addSomeGitRepoAwatingTermination();

        assertThat(project.getLastUpdate().get().getCurrentStatus()).isEqualTo(ProjectStatus.FAILED);
    }   
    
    @Test
    public void addingANewProjectWillLookForUnusedPrivateVariablesInTheEntityRepo() throws Exception {
        addSomeGitRepoAwatingTermination();

        verify(unusedPrivateVariable, times(1)).getEntities(entityRepo);
    }   
    
    @Test
    public void addingANewProjectWillLookForUnusedPrivateMethodsInTheEntityRepo() throws Exception {
        addSomeGitRepoAwatingTermination();

        verify(unusedPrivateMethod, times(1)).getEntities(entityRepo);
    }   
    
    @Test
    public void addingANewProjectWillLookForUnusedParameterInTheEntityRepo() throws Exception {
        addSomeGitRepoAwatingTermination();

        verify(unusedParameter, times(1)).getEntities(entityRepo);
    }  
    
    @Test
    public void whenDatabaseIsNotAvailableProjectStatusShouldBeFailed() throws Exception {
        projectAnalyzer.setEntityRepoFactory(new EntityRepoFactory() {
            @Override
            public EntityRepo newEntityRepo(String udbFile) throws IOException {
                throw new IOException();
            }
        });

        addSomeGitRepoAwatingTermination();

        assertThat(project.getLastUpdate().get().getCurrentStatus()).isEqualTo(ProjectStatus.FAILED);
    }    
    
    @Test
    public void addingANewProjectSuccessfullyWillChangeStatusToCompleted() throws Exception {
        addSomeGitRepoAwatingTermination();

        assertThat(project.getLastUpdate().get().getCurrentStatus()).isEqualTo(ProjectStatus.COMPLETED);
    } 
    
    @Test
    public void whenEntityIsReturnedByOneOfTheFiltersADefectIsLoggedOnTheProject() throws Exception {
        ProjectEntity defectEntity = mock(ProjectEntity.class);
        when(defectEntity.refs(null, null, true)).thenReturn(new Reference[]{reference});
        when(unusedPrivateVariable.getEntities(entityRepo)).thenReturn(Arrays.asList(defectEntity));
        
        addSomeGitRepoAwatingTermination();
        
        assertThat(project.getLastUpdate().get().getDefects().get(0).getDefectType()).isEqualTo(DefectType.UNUSED_PRIVATE_VARIABLE);
    }
    
    @Test
    public void whenUpdatingProjectIsAnalyzedAgain() throws Exception {
        projectAnalyzer.addProject("/some/git/repo");
        projectAnalyzer.updateProject(project);
        shutdownExecutorAwatingTermination();
        
        verify(analyzeCommand, times(2)).analyze(any(File.class), any(File.class));
    }
    
    private File repoBrowserFile() throws GitAPIException {
        File projectFile = new File("some-file");
        when(repoBrowser.downloadProject(any(String.class))).thenReturn(projectFile);
        return projectFile;
    }

    private void addSomeGitRepoAwatingTermination() throws InterruptedException {
        projectAnalyzer.addProject("/some/git/repo");
        shutdownExecutorAwatingTermination();
    }

    private void shutdownExecutorAwatingTermination() throws InterruptedException {
        executor.getThreadPoolExecutor().shutdown();
        executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);
    }
}
