package com.aurea.rest;

public class ProjectNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;
    
    private final JsonExceptionMessage jsonExceptionMessage;

    public ProjectNotFoundException(String projectId) {
        super(projectId);
        jsonExceptionMessage = new JsonExceptionMessage();
        jsonExceptionMessage.setError("Project not found: "+projectId);
    }

    public JsonExceptionMessage getJsonExceptionMessage() {
        return jsonExceptionMessage;
    }
}
