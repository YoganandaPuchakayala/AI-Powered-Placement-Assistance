package com.aipoweredplacementassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aipoweredplacementassistant.dto.CoverLetterRequest;
import com.aipoweredplacementassistant.service.CoverLetterService;
import com.aipoweredplacementassistant.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/coverletter")
public class CoverLetterController {

    @Autowired
    private CoverLetterService coverLetterService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/generate")
    public String generateCoverLetter(
            @RequestBody CoverLetterRequest request,
            HttpServletRequest httpRequest) {

        String token =
                httpRequest
                .getHeader("Authorization")
                .substring(7);

        String email =
                jwtUtil.extractEmail(token);

        return coverLetterService.generateCoverLetter(
                email,
                request.getJobDescription());
    }
}