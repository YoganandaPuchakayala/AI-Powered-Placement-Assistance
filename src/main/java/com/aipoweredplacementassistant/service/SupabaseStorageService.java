package com.aipoweredplacementassistant.service;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class SupabaseStorageService {

    private final String SUPABASE_URL = "https://bbliikehvpgdgqbpaxqj.supabase.co";
    private final String SUPABASE_BUCKET = "resumes";
    private final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJibGlpa2VodnBnZGdxYnBheHFqIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc4MTUwNTA5MSwiZXhwIjoyMDk3MDgxMDkxfQ.5Un6fR8Khc7utA_sfzY4d4k8pRbcGBJOJIV2NkSXQkU";

    public String upload(MultipartFile file) {

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            URL url = new URL(SUPABASE_URL + "/storage/v1/object/"+SUPABASE_BUCKET+"/" + fileName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setRequestProperty("Content-Type", file.getContentType());
            conn.setRequestProperty("x-upsert", "true");

            try (InputStream is = file.getInputStream()) {
                is.transferTo(conn.getOutputStream());
            }

            int responseCode = conn.getResponseCode();

            if (responseCode != 200 && responseCode != 201) {
                throw new RuntimeException("Supabase upload failed");
            }
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(conn.getInputStream());

            String fullPath = node.get("Key").asText();

            return SUPABASE_URL + "/storage/v1/object/public/"
            + fullPath;

        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }
}