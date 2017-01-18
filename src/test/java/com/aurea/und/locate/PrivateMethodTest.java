package com.aurea.und.locate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aurea.repo.ProjectEntity;


public class PrivateMethodTest {
    
    private PrivateMethod privateMethod;
    @Mock
    private ProjectEntity entity;
    
    @Before
    public void setUp() {
        privateMethod = new PrivateMethod();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void currentClassIsOwnEntity() {
        when(entity.longname(true)).thenReturn(this.getClass().getCanonicalName());
        
        assertThat(privateMethod.isOwnEntity(entity)).isTrue();
    }
    
    @Test
    public void javaUtilClassIsNotOwnEntity() {
        when(entity.longname(true)).thenReturn("java.util.List");
        
        assertThat(privateMethod.isOwnEntity(entity)).isFalse();
    }
    
    @Test
    public void javaxClassIsNotOwnEntity() {
        when(entity.longname(true)).thenReturn("javax.mail.Transport");
        
        assertThat(privateMethod.isOwnEntity(entity)).isFalse();
    }

    @Test
    public void sunClassIsNotOwnEntity() {
        when(entity.longname(true)).thenReturn("sun.misc.Unsafe");
        
        assertThat(privateMethod.isOwnEntity(entity)).isFalse();
    }
}
