package com.internship.tool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Service
public class AiServiceClient {

    // Used to make HTTP calls
    private final RestTemplate restTemplate = new RestTemplate();

    // Your Flask API endpoint
    private final String AI_URL = "http://localhost:5000/api/generate-report";

    // Method to call AI service
    public Map<String, Object> generateReport(String input) {
        try {
            // Set request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Request body
            Map<String, String> body = Map.of("input", input);

            // Combine headers + body
            HttpEntity<Map<String, String>> request =
                    new HttpEntity<>(body, headers);

            // Make POST request
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(AI_URL, request, Map.class);

            // Return response
            return response.getBody();

        } catch (Exception e) {
            System.out.println("AI call failed: " + e.getMessage());

            // VERY IMPORTANT → avoid crashing service
            return null;
        }
    }
}