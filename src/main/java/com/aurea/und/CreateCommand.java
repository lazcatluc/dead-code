package com.aurea.und;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateCommand {

    private static final Logger LOGGER = Logger.getLogger(CreateCommand.class);

    @Value("${undCommand}")
    private String undCommand;

    public Process createUdb(File executionFolder, String projectAnalysisFolder, String dbFileName) throws IOException {
        String command = undCommand + " create -db " + dbFileName + " -languages java add " + projectAnalysisFolder;
        LOGGER.info("Running process: " + command);
        return Runtime.getRuntime().exec(command, new String[] {}, executionFolder);
    }

}
