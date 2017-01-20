package com.aurea.und;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeCommand {

    private static final Logger LOGGER = Logger.getLogger(AnalyzeCommand.class);

    @Value("${undCommand}")
    private String undCommand;

    public Process analyze(File executionFolder, File udbFileToAnalyze) throws IOException {
        List<String> command = Arrays.asList(undCommand, "-db", udbFileToAnalyze.getCanonicalPath(), "analyze");
        LOGGER.info("Running process: " + command);
        ProcessBuilder pb = new ProcessBuilder(command).redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT).directory(executionFolder);
        return pb.start();
    }

}
