package com.aipoweredplacementassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.aipoweredplacementassistant.dto.UserResponse;
import com.aipoweredplacementassistant.dto.UserUpdateRequest;
import com.aipoweredplacementassistant.entity.User;
import com.aipoweredplacementassistant.repository.UserRepository;
import com.aipoweredplacementassistant.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {
	
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/me")
    public UserResponse getCurrentUser(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getEducation()
        );
    }
    
    @PutMapping("/me")
    public UserResponse updateUser(
            HttpServletRequest request,
            @RequestBody UserUpdateRequest updateRequest) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // update fields
        if (updateRequest.getFullName() != null) {
            user.setFullName(updateRequest.getFullName());
        }

        if (updateRequest.getPhone() != null) {
            user.setPhone(updateRequest.getPhone());
        }

        if (updateRequest.getEducation() != null) {
            user.setEducation(updateRequest.getEducation());
        }

        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getEducation()
        );
    }

}
