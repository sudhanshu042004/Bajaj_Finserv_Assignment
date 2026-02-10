package com.bajaj.backend.controller;

import com.bajaj.backend.dto.ApiRequest;
import com.bajaj.backend.dto.ApiResponse;
import com.bajaj.backend.service.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*") // Allow public accessibility
public class ApiController {

    @Autowired
    private LogicService logicService;

    // Email config
    private static final String DEFAULT_EMAIL = "sudhanshu1515.be23@chitkarauniversity.edu.in";

    private String getOfficialEmail() {
        String envEmail = System.getenv("OFFICIAL_EMAIL");
        return (envEmail != null && !envEmail.isEmpty()) ? envEmail : DEFAULT_EMAIL;
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> healthCheck() {
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .officialEmail(getOfficialEmail())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bfhl")
    public ResponseEntity<ApiResponse> handlePost(@RequestBody ApiRequest request) {
        System.out.println("Received Request: " + request);
        // Only one of fibonacci, prime, lcm, hcf, AI should be present
        // Or handle all if present? Requirement says "Each request will contain exactly
        // one of".
        // But we should probably check which one is non-null.

        Object data = null;

        try {
            if (request.getFibonacci() != null) {
                data = logicService.calculateFibonacci(request.getFibonacci());
            } else if (request.getPrime() != null) {
                data = logicService.filterPrimes(request.getPrime());
            } else if (request.getLcm() != null) {
                data = logicService.calculateLcm(request.getLcm());
            } else if (request.getHcf() != null) {
                data = logicService.calculateHcf(request.getHcf());
            } else if (request.getAI() != null) {
                data = logicService.getAiResponse(request.getAI());
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .success(false)
                        .officialEmail(getOfficialEmail())
                        .data("Invalid Request: No valid key found")
                        .build());
            }

            ApiResponse response = ApiResponse.builder()
                    .success(true)
                    .officialEmail(getOfficialEmail())
                    .data(data)
                    .build();

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .officialEmail(getOfficialEmail())
                    .data(e.getMessage())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.builder()
                    .success(false)
                    .officialEmail(getOfficialEmail())
                    .data("Error processing request: " + e.getMessage())
                    .build());
        }
    }
}
