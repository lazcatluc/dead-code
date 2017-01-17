package com.aurea.repo;

import java.io.File;
import java.util.UUID;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
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
     * @throws TransportException 
     * @throws InvalidRemoteException
     * 
     * @return the folder that we have to parse.
     */
    public File downloadProject(String projectUrl) throws InvalidRemoteException, TransportException, GitAPIException {
        String uuid = UUID.randomUUID().toString();
        File projectFolder = new File(workFolder, uuid);
        Git.cloneRepository()
            .setURI(projectUrl)
            .setDirectory(projectFolder)
            .call();
        return projectFolder;
    }
    
    /**
     * Updates a current GIT repo
     */
    public void updateFolder(String projectFolder) {
        
    }
}
