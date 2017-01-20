package com.aurea.und;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateCommand {

    private static final Logger LOGGER = Logger.getLogger(CreateCommand.class);

    @Value("${undCommand}")
    private String undCommand;

    public Process createUdb(File executionFolder, String projectAnalysisFolder, String dbFileName) throws IOException {
        List<String> command = Arrays.asList(undCommand, "create", "-db", dbFileName, "-languages", "java", "add",
                projectAnalysisFolder);
        LOGGER.info("Running process: " + command);
        ProcessBuilder pb = new ProcessBuilder(command).redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT).directory(executionFolder);
        return pb.start();
    }

}
