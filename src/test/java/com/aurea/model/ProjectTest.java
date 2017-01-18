package com.aurea.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest {
    
    private Project project;

    @Before
    public void setUp() {
        project = new Project();
    }

    @Test
    public void getSetPath() throws Exception {
        project.setPath("/some/path");
        
        assertThat(project.getPath()).isEqualTo("/some/path");
    }
    
    @Test
    public void getUpdates() throws Exception {
        project.setUpdates(Arrays.asList(new UpdateAction()));
        
        assertThat(project.getUpdates()).isEqualTo(Arrays.asList(new UpdateAction()));
    }
    
    @Test
    public void canAddUpdate() throws Exception {
        project.addUpdate(new UpdateAction());
        
        assertThat(project.getUpdates()).isEqualTo(Arrays.asList(new UpdateAction()));
    }
    
    @Test
    public void getsLastActionAsCurrentAction() throws Exception {
        UpdateAction firstAction = new UpdateAction();
        firstAction.setActionId(1L);
        project.addUpdate(firstAction);
        UpdateAction secondAction = new UpdateAction();
        secondAction.setActionId(2L);
        project.addUpdate(secondAction);
        
        assertThat(project.getLastUpdate().get()).isEqualTo(secondAction);
    }
    
    @Test
    public void getsEmptyOptionalForCurrentActionInitially() throws Exception {
        assertThat(project.getLastUpdate().isPresent()).isEqualTo(false);
    }
    
    @Test
    public void isEqualToSelf() throws Exception {
        assertThat(project).isEqualTo(project);
    }
    
    @Test
    public void isNotEqualToNull() throws Exception {
        assertThat(project).isNotEqualTo(null);
    }
    
    @Test
    public void isNotEqualToMock() throws Exception {
        assertThat(project).isNotEqualTo(mock(Project.class));
    }    
    
    @Test
    public void isEqualToProjectWithUnsetId() throws Exception {
        assertThat(project).isEqualTo(new Project());
    }    
    
    @Test
    public void isNotEqualToProjectWithSetId() throws Exception {
        Project other = new Project();
        other.setProjectId(1L);
        
        assertThat(project).isNotEqualTo(other);
    }     
    
    @Test
    public void hasSameHashCodeAsProjectWithUnsetId() throws Exception {
        assertThat(project.hashCode()).isEqualTo(new Project().hashCode());
    }    
    
    @Test
    public void isEqualToProjectWithSameId() throws Exception {
        project.setProjectId(1L);
        Project expected = new Project();
        expected.setProjectId(1L);
        
        assertThat(project).isEqualTo(expected);
    }    

    @Test
    public void isEqualToProjectWithDifferentId() throws Exception {
        project.setProjectId(1L);
        Project expected = new Project();
        expected.setProjectId(2L);
        
        assertThat(project).isNotEqualTo(expected);
    }
    
    @Test
    public void hasSameHashCodeAsProjectWithSameId() throws Exception {
        project.setProjectId(1L);
        Project expected = new Project();
        expected.setProjectId(1L);
        
        assertThat(project.hashCode()).isEqualTo(expected.hashCode());
    }    
}
