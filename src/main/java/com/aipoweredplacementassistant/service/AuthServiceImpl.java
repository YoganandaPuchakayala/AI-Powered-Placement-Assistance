package com.aipoweredplacementassistant.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aipoweredplacementassistant.dto.LoginRequest;
import com.aipoweredplacementassistant.dto.SignupRequest;
import com.aipoweredplacementassistant.entity.User;
import com.aipoweredplacementassistant.exceptions.AuthResponse;
import com.aipoweredplacementassistant.exceptions.InvalidCredentialsException;
import com.aipoweredplacementassistant.exceptions.ResourceNotFoundException;
import com.aipoweredplacementassistant.exceptions.UserAlreadyExistsException;
import com.aipoweredplacementassistant.repository.UserRepository;
import com.aipoweredplacementassistant.util.JwtUtil;


@Service
public class AuthServiceImpl implements AuthServiceInterface {

	@Autowired
    private  UserRepository userRepository;
	
	
	@Autowired
    private  PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
    
    
    @Override
    public AuthResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User already exists with email: "
                            + request.getEmail());
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return new AuthResponse("User registered successfully");
    }
    
    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + request.getEmail()));

        boolean isPasswordValid = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword());

        if (!isPasswordValid) {
            throw new InvalidCredentialsException(
                    "Invalid email or password");
        }
        
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(
                "Login successful | JWT generated",
                token
        );
    }


}