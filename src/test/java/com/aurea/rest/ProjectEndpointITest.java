package com.aurea.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectEndpointITest {

    private static final Logger LOGGER = Logger.getLogger(ProjectEndpointITest.class);

    @Autowired
    private TestRestTemplate restTemplate;
    private String addedProjectId;

    @Test
    public void getsProjects() throws InterruptedException {
        ProjectUrl projectUrl = new ProjectUrl();
        projectUrl.setUrl("https://github.com/lazcatluc/conway");
        ResponseEntity<RestProject> addedProject = restTemplate.postForEntity("/api/projects",
                projectUrl, RestProject.class);
        assertThat(addedProject.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOGGER.info(addedProject.getBody());
        addedProjectId = addedProject.getBody().getProjectId();
        
        ResponseEntity<String> response = restTemplate.getForEntity("/api/projects", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOGGER.info(response.getBody());
        
        response = restTemplate.getForEntity("/api/projects/"+addedProjectId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOGGER.info(response.getBody());
        
        response = restTemplate.getForEntity("/api/projects/some-other-project", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        LOGGER.info(response.getBody());
        
        response = restTemplate.postForEntity("/api/projects/"+addedProjectId, "", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        LOGGER.info(response.getBody());
        
        Thread.sleep(30000);
        
        response = restTemplate.postForEntity("/api/projects/"+addedProjectId, "", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOGGER.info(response.getBody());
        
        Thread.sleep(30000);
        
        response = restTemplate.getForEntity("/api/projects/"+addedProjectId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOGGER.info(response.getBody());
    }

    @After
    public void cleanUp() {
        if (addedProjectId != null) {
            restTemplate.delete("/api/projects/"+addedProjectId);
        }
    }
}
