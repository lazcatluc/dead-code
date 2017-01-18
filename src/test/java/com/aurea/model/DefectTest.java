package com.aurea.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class DefectTest {
    
    private Defect defect;
    
    @Before
    public void setUp() {
        defect = new Defect();
    }

    @Test
    public void getSetDefectType() {
        defect.setDefectType(DefectType.UNUSED_PARAMETER);
        
        assertThat(defect.getDefectType()).isEqualTo(DefectType.UNUSED_PARAMETER);
    }

    @Test
    public void getSetProjectFile() {
        defect.setProjectFile("MyClass.java");
        
        assertThat(defect.getProjectFile()).isEqualTo("MyClass.java");
    }
    
    @Test
    public void getSetDefectLine() {
        defect.setDefectLine(2);
        
        assertThat(defect.getDefectLine()).isEqualTo(2);
    }
    
    @Test
    public void getSetDefectColumn() {
        defect.setDefectColumn(2);
        
        assertThat(defect.getDefectColumn()).isEqualTo(2);
    }

    @Test
    public void getSetUpdateAction() {
        defect.setUpdateAction(new UpdateAction());
        
        assertThat(defect.getUpdateAction()).isEqualTo(new UpdateAction());
    } 
    
    @Test
    public void getSetEntityName() {
        defect.setEntityName("myMethod");
        
        assertThat(defect.getEntityName()).isEqualTo("myMethod");
    }
    
    @Test
    public void isEqualToSelf() throws Exception {
        assertThat(defect).isEqualTo(defect);
    }
    
    @Test
    public void isNotEqualToNull() throws Exception {
        assertThat(defect).isNotEqualTo(null);
    }
    
    @Test
    public void isNotEqualToMock() throws Exception {
        assertThat(defect).isNotEqualTo(mock(Defect.class));
    }
    
    @Test
    public void isEqualToObjectWithNullId() throws Exception {
        assertThat(defect).isEqualTo(new Defect());
    }
    
    @Test
    public void hasSameHashCodeToObjectWithNullId() throws Exception {
        assertThat(defect.hashCode()).isEqualTo(new Defect().hashCode());
    }
    
    @Test
    public void isEqualToObjectWithSameId() throws Exception {
        defect.setDefectId(2L);
        Defect expected = new Defect();
        expected.setDefectId(2L);
        
        assertThat(defect).isEqualTo(expected);
    }
    
    @Test
    public void hasSameHashCodeToObjectWithSameId() throws Exception {
        defect.setDefectId(2L);
        Defect expected = new Defect();
        expected.setDefectId(2L);
        
        assertThat(defect.hashCode()).isEqualTo(expected.hashCode());
    }
    
    @Test
    public void isEqualToObjectWithDifferentId() throws Exception {
        defect.setDefectId(2L);
        Defect expected = new Defect();
        expected.setDefectId(1L);
        
        assertThat(defect).isNotEqualTo(expected);
    }
    
    @Test
    public void isEqualToObjectWithSetId() throws Exception {
        Defect expected = new Defect();
        expected.setDefectId(1L);
        
        assertThat(defect).isNotEqualTo(expected);
    }
}
