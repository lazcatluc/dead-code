package com.aurea.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectEndpointTest {

    private static final Logger LOGGER = Logger.getLogger(ProjectEndpointTest.class);

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Test(timeout = 30000)
    public void getsProjects() throws InterruptedException {
        ProjectUrl projectUrl = new ProjectUrl();
        projectUrl.setUrl("https://github.com/lazcatluc/conway");
        ResponseEntity<RestProject> addedProject = restTemplate.postForEntity("/api/projects",
                projectUrl, RestProject.class);
        assertThat(addedProject.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOGGER.info(addedProject.getBody());
        
        ResponseEntity<String> response = restTemplate.getForEntity("/api/projects", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOGGER.info(response.getBody());
        
        response = restTemplate.getForEntity("/api/projects/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOGGER.info(response.getBody());
        
        response = restTemplate.getForEntity("/api/projects/2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        LOGGER.info(response.getBody());
        
        response = restTemplate.postForEntity("/api/projects/1", "", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        LOGGER.info(response.getBody());
        
        while (executor.getActiveCount() > 0) {
            Thread.sleep(1000);
        }
        
        response = restTemplate.postForEntity("/api/projects/1", "", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOGGER.info(response.getBody());
    }

}
