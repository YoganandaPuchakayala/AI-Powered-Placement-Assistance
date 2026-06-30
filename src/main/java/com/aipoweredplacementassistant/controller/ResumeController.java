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

    @PostMapping("/uploadresume")
    public ResumeResponse uploadResume(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        String email = jwtUtil.extractEmail(token);

        return resumeService.uploadResume(email, file);
    }

    
    @GetMapping("/readmyresume")
    public ResumeResponse getMyResume(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);

        Resume resume = resumeRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return new ResumeResponse(
                "Resume fetched successfully",
                resume.getFileUrl(), resume.getFileName()
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
