package com.aurea.repo;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RepoBrowser {
    
    @Value("${workFolder}")
    private File workFolder;
    
    /**
     * Downloads a GIT repo
     * 
     * @throws GitAPIException 
     * 
     * @return the folder that we have to parse.
     */
    public File downloadProject(String projectUrl, String projectId) throws GitAPIException {
        File projectFolder = new File(workFolder, projectId);
        
        Git.cloneRepository()
                .setURI(projectUrl)
                .setDirectory(projectFolder)
                .call();
       
        return projectFolder;
    }
    
    /**
     * Updates a current GIT repo
     * @throws IOException 
     */
    public void updateFolder(File projectFolder) throws IOException, GitAPIException {
        Git.open(projectFolder).pull().call();
    }
}
