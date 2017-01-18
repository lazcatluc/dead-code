package com.aurea.repo;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import com.aurea.und.ProjectAnalyzer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectAnalyzerITest {
    @Autowired
    private ProjectAnalyzer projectAnalyzer;
    @Autowired
    private ThreadPoolTaskExecutor executor;
    
    @Test
    public void analyzesPublicGithubProject() throws Exception {
        projectAnalyzer.addProject("https://github.com/lazcatluc/conway");
        executor.shutdown();
        executor.getThreadPoolExecutor().awaitTermination(30, TimeUnit.SECONDS);
    }
}
