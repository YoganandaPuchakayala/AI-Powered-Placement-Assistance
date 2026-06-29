package com.aipoweredplacementassistant.exceptions;


public class AuthResponse {

    private String message;
    private String token;

    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public AuthResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}