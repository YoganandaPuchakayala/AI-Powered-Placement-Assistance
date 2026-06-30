package com.aipoweredplacementassistant.dto;

public class ResumeResponse {

    private String message;
    private String data;
    private String fileName;

    public ResumeResponse(String message, String data, String fileName) {
        this.message = message;
        this.data = data;
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public String getdata() {
        return data;
    }

    public String getfileName(){
        return fileName;
    }
}
