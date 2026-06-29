package com.aipoweredplacementassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aipoweredplacementassistant.dto.LoginRequest;
import com.aipoweredplacementassistant.dto.SignupRequest;
import com.aipoweredplacementassistant.exceptions.AuthResponse;
import com.aipoweredplacementassistant.service.AuthServiceInterface;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
	
	@Autowired
	private AuthServiceInterface authService;
	
	
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request){
		
		AuthResponse response = authService.signup(request);
		
		return ResponseEntity.ok(response);
	}
	
	
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

	 @GetMapping("/")
    public String health() {
        return "AI Placement Assistant API Running";
    }

}
