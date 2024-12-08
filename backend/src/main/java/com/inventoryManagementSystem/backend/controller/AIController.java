package com.example.proxy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class AIController {

    @Value("${cloudflare.api.url}")
    private String cloudflareApiUrl;

    @Value("${cloudflare.api.key}")
    private String cloudflareApiKey;

    @PostMapping("/insights")
    public ResponseEntity<String> getInsights(@RequestBody String body) {
        RestTemplate restTemplate = new RestTemplate();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + cloudflareApiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        // Create the request
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            // Forward the request to the Cloudflare API
            ResponseEntity<String> response = restTemplate.exchange(
                    cloudflareApiUrl, HttpMethod.POST, request, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            // Handle errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}