package com.aipoweredplacementassistant.service;

public interface JobMatchService {

    String compareResumeWithJob(
            String email,
            String jobDescription);
}