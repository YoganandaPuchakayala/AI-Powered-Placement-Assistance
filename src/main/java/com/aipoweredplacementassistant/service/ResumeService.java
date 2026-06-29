package com.aipoweredplacementassistant.service;

import org.springframework.web.multipart.MultipartFile;

import com.aipoweredplacementassistant.dto.ResumeResponse;

public interface ResumeService {

    ResumeResponse uploadResume(String email, MultipartFile file);
}