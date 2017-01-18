package com.aurea.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private InitializedProjects projectRepository;

    @Test
    public void analyzesPublicGithubProjectAndCompletes() throws Exception {
        projectAnalyzer.addProject("https://github.com/lazcatluc/conway");
        executor.shutdown();
        executor.getThreadPoolExecutor().awaitTermination(30, TimeUnit.SECONDS);

        List<Project> projects = projectRepository.findAllInitialized();

        LOGGER.info("Projects: " + projects);
        assertThat(projects.size()).isEqualTo(1);
        assertThat(projects.get(0).getLastUpdate().get().getCurrentStatus()).isNotEqualTo(ProjectStatus.PROCESSING);
    }
}
