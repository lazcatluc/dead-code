package com.aurea.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepoBrowserTest {

    @Autowired
    private RepoBrowser repoBrowser;
    private File localRepo;
    
    @Test
    public void downloadsPublicRepoFromGithub() throws Exception {
        localRepo = repoBrowser.downloadProject("https://github.com/lazcatluc/conway", "1");
        
        assertThat(localRepo.exists()).isTrue();
        
        repoBrowser.updateFolder(localRepo);
    }
    
    @After
    public void cleanUp() throws Exception {
        Cleaner.cleanUpRecursively(localRepo);
    }

}
