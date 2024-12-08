package com.inventoryManagementSystem.backend.controller;

import lombok.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/insights")
public class InsightsController {

    private String cloudflareApiKey="8_HvhWtvvgjyrt4qb1IsJtIatochSH_QYtefqsv4";
    private String cloudflareApiUrl="https://api.cloudflare.com/client/v4/accounts/ddadeabed2494154764db552710a34c2/ai/run/@cf/meta/llama-3.1-8b-instruct";

    @PostMapping("/apiResponse")
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
