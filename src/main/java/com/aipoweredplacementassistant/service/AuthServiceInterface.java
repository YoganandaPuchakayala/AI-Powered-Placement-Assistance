package com.aipoweredplacementassistant.service;

import com.aipoweredplacementassistant.dto.LoginRequest;
import com.aipoweredplacementassistant.dto.SignupRequest;
import com.aipoweredplacementassistant.exceptions.AuthResponse;

public interface AuthServiceInterface {
	
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);

}
