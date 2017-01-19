package com.aurea.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aurea.model.Project;
import com.aurea.model.ProjectStatus;
import com.aurea.und.ProjectAnalyzer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectAnalyzerITest {

    private static final Logger LOGGER = Logger.getLogger(ProjectAnalyzerITest.class);

    @Autowired
    private ProjectAnalyzer projectAnalyzer;
    @Autowired
    private InitializedProjects projectRepository;
    @Autowired
    private ProjectCleaner projectCleaner;
    private Project project;

    @Test
    public void analyzesPublicGithubProjectAndCompletes() throws Exception {
        projectAnalyzer.addProject("https://github.com/lazcatluc/conway");
        Thread.sleep(30000);
        List<Project> projects = projectRepository.findAllInitialized();

        LOGGER.info("Projects: " + projects);
        assertThat(projects.size()).isEqualTo(1);
        project = projects.get(0);
        assertThat(project.getLastUpdate().get().getCurrentStatus()).isNotEqualTo(ProjectStatus.PROCESSING);
    }
    
    @After
    public void cleanUp() {
        if (project != null) {
            projectCleaner.clean(project.getProjectId());
        }
    }

}
