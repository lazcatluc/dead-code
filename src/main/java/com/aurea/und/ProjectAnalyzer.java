package com.aurea.und;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

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

@Service
public class ProjectAnalyzer {

    private static final Logger LOGGER = Logger.getLogger(ProjectAnalyzer.class);

    @Value("${workFolder}")
    private File workFolder;
    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private RepoBrowser repoBrowser;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CreateCommand createCommand;
    @Autowired
    private AnalyzeCommand analyzeCommand;
    @Autowired
    private DeadCodeModelFactory modelFactory;
    @Autowired
    private EntityRepoFactory entityRepoFactory;
    @Autowired
    private UnusedPrivateMethod unusedPrivateMethod;
    @Autowired
    private UnusedPrivateVariable unusedPrivateVariable;
    @Autowired
    private UnusedParameter unusedParameter;

    public void setWorkFolder(File workFolder) {
        this.workFolder = workFolder;
    }

    public void setExecutor(ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    public void setRepoBrowser(RepoBrowser repoBrowser) {
        this.repoBrowser = repoBrowser;
    }

    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void setCreateCommand(CreateCommand createCommand) {
        this.createCommand = createCommand;
    }

    public void setAnalyzeCommand(AnalyzeCommand analyzeCommand) {
        this.analyzeCommand = analyzeCommand;
    }

    public void setModelFactory(DeadCodeModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    public void setEntityRepoFactory(EntityRepoFactory entityRepoFactory) {
        this.entityRepoFactory = entityRepoFactory;
    }

    public UnusedPrivateMethod getUnusedPrivateMethod() {
        return unusedPrivateMethod;
    }

    public void setUnusedPrivateMethod(UnusedPrivateMethod unusedPrivateMethod) {
        this.unusedPrivateMethod = unusedPrivateMethod;
    }

    public UnusedPrivateVariable getUnusedPrivateVariable() {
        return unusedPrivateVariable;
    }

    public void setUnusedPrivateVariable(UnusedPrivateVariable unusedPrivateVariable) {
        this.unusedPrivateVariable = unusedPrivateVariable;
    }

    public UnusedParameter getUnusedParameter() {
        return unusedParameter;
    }

    public void setUnusedParameter(UnusedParameter unusedParameter) {
        this.unusedParameter = unusedParameter;
    }

    public Project addProject(String projectUrl) {
        Project project = modelFactory.newProject();
        project.addUpdate(modelFactory.newUpdateAction());
        Project savedProject = projectRepository.save(project);
        executor.execute(() -> addProject(savedProject, projectUrl));
        return savedProject;
    }

    public Project updateProject(Project project) {
        project.addUpdate(modelFactory.newUpdateAction());
        Project savedProject = projectRepository.save(project);

        executor.execute(() -> analyzeProject(savedProject, projectUdbFile(project)));
        return savedProject;
    }

    private File projectUdbFile(Project project) {
        return new File(workFolder, project.getProjectId() + ".udb");
    }

    private void addProject(Project project, String projectUrl) {
        try {
            File localRepo = repoBrowser.downloadProject(projectUrl);
            project.setPath(localRepo.getCanonicalPath());
            Project savedProject = projectRepository.save(project);
            createProjectUdb(savedProject);
        } catch (GitAPIException | IOException e) {
            LOGGER.error(e);
            failWithException(project, e);
        }
    }

    private void createProjectUdb(Project project) {
        try {
            Process createProcess = createCommand.createUdb(workFolder, project.getPath(),
                    project.getProjectId() + ".udb");

            int exitCode = createProcess.waitFor();
            if (exitCode != 0) {
                failWithReason(project, "Create UDB failed. Exit code: " + exitCode);
                return;
            }
            analyzeProject(project, projectUdbFile(project));
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e);
            failWithException(project, e);
        }
    }

    private void analyzeProject(Project project, File udbFileToAnalyze) {
        try {
            Process createProcess = analyzeCommand.analyze(workFolder, udbFileToAnalyze);

            int exitCode = createProcess.waitFor();
            if (exitCode != 0) {
                failWithReason(project, "Analyze UDB failed. Exit code: " + exitCode);
                return;
            }
            findDefects(project, udbFileToAnalyze);
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e);
            failWithException(project, e);
        }

    }

    private void findDefects(Project project, File udbFileToAnalyze) {
        try (EntityRepo entityRepo = entityRepoFactory.newEntityRepo(udbFileToAnalyze.getCanonicalPath())) {
            unusedPrivateVariable.getEntities(entityRepo)
                    .forEach(entity -> logProjectDefect(project, entity, DefectType.UNUSED_PRIVATE_VARIABLE));
            unusedPrivateMethod.getEntities(entityRepo)
                    .forEach(entity -> logProjectDefect(project, entity, DefectType.UNUSED_PRIVATE_METHOD));
            unusedParameter.getEntities(entityRepo)
                    .forEach(entity -> logProjectDefect(project, entity, DefectType.UNUSED_PARAMETER));
            UpdateAction action = modelFactory.newUpdateAction();
            action.setCurrentStatus(ProjectStatus.COMPLETED);
            project.addUpdate(action);
            projectRepository.save(project);
        } catch (IOException e) {
            LOGGER.error(e);
            failWithException(project, e);
        }
    }

    private void logProjectDefect(Project project, ProjectEntity entity, DefectType defectType) {
        Defect defect = modelFactory.newDefect();
        defect.setDefectType(defectType);
        defect.setEntityName(entity.longname(true));
        Reference reference = entity.refs(null, null, true)[0];
        defect.setProjectFile(reference.file().longname(true));
        defect.setDefectLine(reference.line());
        defect.setDefectColumn(reference.column());
        project.getLastUpdate().get().addDefect(defect);
    }

    private void failWithException(Project project, Exception e) {
        failWithReason(project, e.getMessage());
    }

    private void failWithReason(Project project, String message) {
        UpdateAction action = modelFactory.newUpdateAction();
        action.setCurrentStatus(ProjectStatus.FAILED);
        action.setFailureReason(message);
        project.addUpdate(action);
        projectRepository.save(project);
    }

}
