package com.aurea.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.aurea.model.Defect;
import com.aurea.model.Project;
import com.aurea.model.ProjectStatus;
import com.aurea.model.UpdateAction;

public class RestProjectTest {
    
    private Project project;
    private UpdateAction updateAction;
    
    @Before
    public void setUp() {
        project = new Project();
        updateAction = new UpdateAction();
        project.addUpdate(updateAction);
    }

    @Test
    public void getsProjectIdOfProject() {
        project.setProjectId(1L);
        
        assertThat(newRestProject().getProjectId()).isEqualTo(1L);
    }

    private RestProject newRestProject() {
        return new RestProject(project);
    }
    
    @Test
    public void getsStatusOfAction() throws Exception {
        updateAction.setCurrentStatus(ProjectStatus.COMPLETED);
        
        assertThat(newRestProject().getStatus()).isEqualTo(ProjectStatus.COMPLETED);
    }

    @Test
    public void getsTimestampOfAction() throws Exception {
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        updateAction.setActionTime(time);
        
        assertThat(newRestProject().getLastUpdated()).isEqualTo(time);
    }
    
    @Test
    public void getsDefectsOfAction() throws Exception {
        Defect defect = mock(Defect.class);
        updateAction.setDefects(Arrays.asList(defect));
        
        assertThat(newRestProject().getDefects()).isEqualTo(Arrays.asList(defect));
    }
}
