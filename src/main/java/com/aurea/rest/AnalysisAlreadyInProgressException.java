package com.aurea.rest;

public class AnalysisAlreadyInProgressException extends IllegalStateException {

    private static final long serialVersionUID = 1L;
    private final JsonExceptionMessage jsonExceptionMessage;

    public AnalysisAlreadyInProgressException(String projectId) {
        super(projectId);
        jsonExceptionMessage = new JsonExceptionMessage();
        jsonExceptionMessage.setError("Analysis already in progress for project "+projectId);
    }

    public JsonExceptionMessage getJsonExceptionMessage() {
        return jsonExceptionMessage;
    }

}
