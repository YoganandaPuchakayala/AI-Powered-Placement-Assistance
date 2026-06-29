package com.aipoweredplacementassistant.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public String analyzeResume(String resumeText) {

        RestTemplate restTemplate = new RestTemplate();

        String prompt = """
            Analyze the following resume and return ONLY valid JSON.

            {
              "name":"",
              "email":"",
              "phone":"",
              "skills":[],
              "experienceYears":0,
              "education":[],
              "summary":""
            }

            Resume:
            """ + resumeText;

        String body = """
        {
          "contents": [{
            "parts": [{
              "text": "%s"
            }]
          }]
        }
        """.formatted(
                prompt.replace("\"", "\\\"")
                      .replace("\n", "\\n")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(body, headers);

        try {
        ResponseEntity<String> response =
                restTemplate.exchange(
                        apiUrl + "?key=" + apiKey,
                        HttpMethod.POST,
                        request,
                        String.class);

        return response.getBody();
        }catch(Exception e) {
        	e.printStackTrace();
        	
        	return "Gemini temporarily unavailable. Please try again.";
        }
    }
    
    
    public String compareResumeWithJob(
            String resumeText,
            String jobDescription) {

        RestTemplate restTemplate = new RestTemplate();

        String prompt = """
        You are an ATS Resume Analyzer.

        Compare the resume with the job description.

        Return ONLY valid JSON.

        {
          "atsScore": 0,
          "matchedSkills": [],
          "missingSkills": [],
          "strengths": [],
          "resumeImprovements": [],
          "recommendedCertifications": [],
          "keywordsToAdd": []
        }

        Resume:
        %s

        Job Description:
        %s
        """.formatted(resumeText, jobDescription);

        try {

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> body =
                    Map.of(
                            "contents",
                            List.of(
                                    Map.of(
                                            "parts",
                                            List.of(
                                                    Map.of("text", prompt)
                                            )
                                    )
                            )
                    );

            String jsonBody =
                    mapper.writeValueAsString(body);

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON);

            HttpEntity<String> request =
                    new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            apiUrl + "?key=" + apiKey,
                            HttpMethod.POST,
                            request,
                            String.class);

            return response.getBody();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Unable to compare resume and JD");
        }
    }
    
    
    public String generateCoverLetter(
            String resumeText,
            String jobDescription) {

        String prompt = """
        Create a professional cover letter.

        Use the candidate's resume and the job description.

        Return ONLY valid JSON.

        {
          "coverLetter":""
        }

        Resume:
        %s

        Job Description:
        %s
        """.formatted(
                resumeText,
                jobDescription);

        return callGemini(prompt);
    }
    
    private String callGemini(String prompt) {

        try {

            RestTemplate restTemplate =
                    new RestTemplate();

            ObjectMapper mapper =
                    new ObjectMapper();

            Map<String, Object> body =
                    Map.of(
                            "contents",
                            List.of(
                                    Map.of(
                                            "parts",
                                            List.of(
                                                    Map.of(
                                                            "text",
                                                            prompt)
                                            )
                                    )
                            )
                    );

            String json =
                    mapper.writeValueAsString(body);

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON);

            HttpEntity<String> request =
                    new HttpEntity<>(json, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            apiUrl + "?key=" + apiKey,
                            HttpMethod.POST,
                            request,
                            String.class);

            return response.getBody();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Gemini API failed");
        }
    }
}
