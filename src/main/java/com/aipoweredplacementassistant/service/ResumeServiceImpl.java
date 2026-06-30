package com.aipoweredplacementassistant.service;

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

        // Upload file to Supabase
        String fileUrl = storageService.upload(file);

        String fileName = file.getOriginalFilename();

        System.out.println("Generated URL = " + fileUrl);

        // Check existing resume
        Resume existingResume = resumeRepository
                .findByUserEmail(email)
                .orElse(null);

        if (existingResume != null) {

            existingResume.setFileName(fileName);
            existingResume.setFileType(file.getContentType());
            existingResume.setFileUrl(fileUrl);

            resumeRepository.save(existingResume);

            return new ResumeResponse(
                    "Resume updated successfully",
                    fileUrl,
                    fileName
            );
        }

        // First time upload
        Resume resume = new Resume();
        resume.setFileName(fileName);
        resume.setFileType(file.getContentType());
        resume.setFileUrl(fileUrl);
        resume.setUser(user);

        resumeRepository.save(resume);

        return new ResumeResponse(
                "Resume uploaded successfully",
                fileUrl,
                fileName
        );
    }
}
