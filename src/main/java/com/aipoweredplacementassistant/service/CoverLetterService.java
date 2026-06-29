package com.aipoweredplacementassistant.service;

public interface CoverLetterService {

    String generateCoverLetter(
            String email,
            String jobDescription);
}