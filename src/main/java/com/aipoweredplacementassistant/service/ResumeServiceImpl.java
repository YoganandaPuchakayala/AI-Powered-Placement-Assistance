package com.aipoweredplacementassistant.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aipoweredplacementassistant.dto.ResumeResponse;
import com.aipoweredplacementassistant.entity.Resume;
import com.aipoweredplacementassistant.entity.User;
import com.aipoweredplacementassistant.repository.ResumeRepository;
import com.aipoweredplacementassistant.repository.UserRepository;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SupabaseStorageService storageService;

    @Override
    public ResumeResponse uploadResume(String email, MultipartFile file) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Upload to Supabase (you will implement this method)
        String fileUrl = storageService.upload(file);
        
        System.out.println("Generated URL = " + fileUrl);

        // 2. Save metadata
        Resume resume = new Resume();
        resume.setFileName(file.getOriginalFilename());
        resume.setFileType(file.getContentType());
        resume.setFileUrl(fileUrl);
        resume.setUser(user);

        resumeRepository.save(resume);

        return new ResumeResponse("Resume uploaded successfully", fileUrl);
    }
}