package com.aipoweredplacementassistant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aipoweredplacementassistant.entity.Resume;
import com.aipoweredplacementassistant.repository.ResumeRepository;

@Service
public class CoverLetterServiceImpl
        implements CoverLetterService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private PdfParserService pdfParserService;

    @Autowired
    private GeminiService geminiService;

    @Override
    public String generateCoverLetter(
            String email,
            String jobDescription) {

        Resume resume =
                resumeRepository
                .findByUserEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resume not found"));

        String resumeText =
                pdfParserService.extractText(
                        resume.getFileUrl());

        return geminiService
                .generateCoverLetter(
                        resumeText,
                        jobDescription);
    }
}
