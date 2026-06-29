package com.aipoweredplacementassistant.dto;

public class ResumeResponse {

    private String message;
    private String data;

    public ResumeResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getdata() {
        return data;
    }
}