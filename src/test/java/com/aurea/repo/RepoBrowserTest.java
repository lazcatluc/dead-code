package com.aurea.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Arrays;

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
        String publicRepoUrl = "https://github.com/lazcatluc/conway";
        
        localRepo = repoBrowser.downloadProject(publicRepoUrl);
        
        assertThat(localRepo.exists()).isTrue();
    }
    
    @After
    public void cleanUp() throws Exception {
        cleanUpRecursively(localRepo);
    }

    private void cleanUpRecursively(File currentFile) {
        if (currentFile == null) {
            return;
        }
        if (currentFile.isDirectory()) {
            Arrays.stream(currentFile.listFiles()).forEach(this::cleanUpRecursively);
        }
        currentFile.delete();
    }

}
