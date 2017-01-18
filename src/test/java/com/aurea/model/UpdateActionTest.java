package com.aurea.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class UpdateActionTest {
    
    private UpdateAction updateAction;
    
    @Before
    public void setUp() {
        updateAction = new UpdateAction();
    }

    @Test
    public void getSetProject() {
        Project project = new Project();
        updateAction.setProject(project);
        
        assertThat(updateAction.getProject()).isEqualTo(project);
    }

    @Test
    public void getSetProjectStatus() {
        ProjectStatus projectStatus = ProjectStatus.FAILED;
        updateAction.setCurrentStatus(projectStatus);
        
        assertThat(updateAction.getCurrentStatus()).isEqualTo(projectStatus);
    }
    
    @Test
    public void getSetDate() {
        LocalDateTime actionTime = LocalDateTime.now().minusDays(1);
        updateAction.setActionTime(actionTime);
        
        assertThat(updateAction.getActionTime()).isEqualTo(actionTime);
    }
    
    @Test
    public void getSetDefects() {
        updateAction.setDefects(Arrays.asList());
        
        assertThat(updateAction.getDefects()).isEqualTo(Arrays.asList());
    }   
    
    @Test
    public void canAddDefect() {
        updateAction.addDefect(new Defect());
        
        assertThat(updateAction.getDefects()).isEqualTo(Arrays.asList(new Defect()));
    }      
    
    @Test
    public void getSetFailureReason() {
        updateAction.setFailureReason("some reason");
        
        assertThat(updateAction.getFailureReason()).isEqualTo("some reason");
    }    
    
    @Test
    public void isEqualToSelf() throws Exception {
        assertThat(updateAction).isEqualTo(updateAction);
    }
    
    @Test
    public void isNotEqualToDifferentClass() throws Exception {
        assertThat(updateAction).isNotEqualTo(mock(UpdateAction.class));
    }
    
    @Test
    public void isEqualToActionWithSameId() throws Exception {
        updateAction.setActionId(1L);
        UpdateAction expected = new UpdateAction();
        expected.setActionId(1L);
        
        assertThat(updateAction).isEqualTo(expected);
    }
    
    @Test
    public void hasSameHashCodeOfActionWithSameId() throws Exception {
        updateAction.setActionId(1L);
        UpdateAction expected = new UpdateAction();
        expected.setActionId(1L);
        
        assertThat(updateAction.hashCode()).isEqualTo(expected.hashCode());
    }
    
    @Test
    public void hasSameHashCodeOfActionWithNullId() throws Exception {
        assertThat(updateAction.hashCode()).isEqualTo(new UpdateAction().hashCode());
    }
    
    @Test
    public void isNotEqualToActionWithDifferentId() throws Exception {
        updateAction.setActionId(1L);
        UpdateAction expected = new UpdateAction();
        expected.setActionId(2L);
        
        assertThat(updateAction).isNotEqualTo(expected);
    }
    
    @Test
    public void isNotEqualToActionWithIdWhenIdNull() throws Exception {
        UpdateAction expected = new UpdateAction();
        expected.setActionId(1L);
        
        assertThat(updateAction).isNotEqualTo(expected);
    }
    
    @Test
    public void isNotEqualToNull() throws Exception {
        assertThat(updateAction).isNotEqualTo(null);
    }
}
