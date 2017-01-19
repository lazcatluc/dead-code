package com.aurea.rest;

public class ProjectNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;
    
    private final JsonExceptionMessage jsonExceptionMessage;

    public ProjectNotFoundException(Long projectId) {
        super(projectId.toString());
        jsonExceptionMessage = new JsonExceptionMessage();
        jsonExceptionMessage.setError("Project not found: "+projectId);
    }

    public JsonExceptionMessage getJsonExceptionMessage() {
        return jsonExceptionMessage;
    }
}
