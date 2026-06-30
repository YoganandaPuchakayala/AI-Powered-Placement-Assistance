package com.aipoweredplacementassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.aipoweredplacementassistant.dto.JobMatchRequest;
import com.aipoweredplacementassistant.dto.ResumeResponse;
import com.aipoweredplacementassistant.entity.Resume;
import com.aipoweredplacementassistant.repository.ResumeRepository;
import com.aipoweredplacementassistant.service.JobMatchService;
import com.aipoweredplacementassistant.service.ResumeAnalysisService;
import com.aipoweredplacementassistant.service.ResumeService;
import com.aipoweredplacementassistant.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;
    
    @Autowired
    private ResumeRepository resumeRepository;
    
    @Autowired
    private ResumeAnalysisService resumeAnalysisService;
    
    @Autowired
    private JobMatchService jobMatchService;

    @Autowired
    private JwtUtil jwtUtil;
    

public ResumeResponse uploadResume(
        String email,
        MultipartFile file) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Resume existingResume = resumeRepository
            .findByUserEmail(email)
            .orElse(null);

    // User already has a resume
    if (existingResume != null) {

        // Delete old file from storage
        supabaseStorageService.deleteFile(
                existingResume.getFileUrl());

        // Upload new file
        String newFileUrl =
                supabaseStorageService.uploadFile(file);

        // Update existing record
        existingResume.setFileUrl(newFileUrl);

        resumeRepository.save(existingResume);

        return new ResumeResponse(
                "Resume updated successfully",
                newFileUrl
        );
    }

    // First upload
    String fileUrl =
            supabaseStorageService.uploadFile(file);

    Resume resume = new Resume();
    resume.setUser(user);
    resume.setFileUrl(fileUrl);

    resumeRepository.save(resume);

    return new ResumeResponse(
            "Resume uploaded successfully",
            fileUrl
    );
}
    
    @GetMapping("/readmyresume")
    public ResumeResponse getMyResume(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);

        Resume resume = resumeRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return new ResumeResponse(
                "Resume fetched successfully",
                resume.getFileUrl()
        );
    }
    
    // ✅ Download (redirect to Supabase)
    @GetMapping("/downloadresume")
    public ResponseEntity<Void> downloadResume(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);

        Resume resume = resumeRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return ResponseEntity.status(302)
                .header("Location", resume.getFileUrl())
                .build();
    }
    
    @GetMapping("/analyze")
    public String analyzeResume(HttpServletRequest request) {

        String token =
                request.getHeader("Authorization")
                       .substring(7);

        String email =
                jwtUtil.extractEmail(token);

        return resumeAnalysisService
                .analyzeResume(email);
    }
    
    
    @PostMapping("/match")
    public String matchResumeWithJob(
            @RequestBody JobMatchRequest request,
            HttpServletRequest httpRequest) {

        String token =
                httpRequest
                .getHeader("Authorization")
                .substring(7);

        String email =
                jwtUtil.extractEmail(token);

        return jobMatchService.compareResumeWithJob(
                email,
                request.getJobDescription());
    }
    
    
}
