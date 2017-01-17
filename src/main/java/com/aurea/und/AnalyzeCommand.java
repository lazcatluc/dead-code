package com.aurea.und;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeCommand {

    private static final Logger LOGGER = Logger.getLogger(AnalyzeCommand.class);

    @Value("${undCommand}")
    private String undCommand;

    public Process analyze(File executionFolder, File udbFileToAnalyze) throws IOException {
        String command = undCommand + " -db " + udbFileToAnalyze.getCanonicalPath() + " analyze -changed";
        LOGGER.info("Running process: " + command);
        return Runtime.getRuntime().exec(command, new String[] {}, executionFolder);
    }

}
