package com.bajaj.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogicService {

    @Value("${app.gemini.api-key}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Integer> calculateFibonacci(int n) {
        List<Integer> fib = new ArrayList<>();
        if (n <= 0)
            return fib;
        fib.add(0);
        if (n == 1)
            return fib;
        fib.add(1);
        for (int i = 2; i < n; i++) {
            fib.add(fib.get(i - 1) + fib.get(i - 2));
        }
        return fib;
    }

    public List<Integer> filterPrimes(List<Integer> numbers) {
        return numbers.stream().filter(this::isPrime).collect(Collectors.toList());
    }

    private boolean isPrime(int num) {
        if (num <= 1)
            return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0)
                return false;
        }
        return true;
    }

    public int calculateLcm(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty())
            return 0;
        int result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = lcm(result, numbers.get(i));
        }
        return result;
    }

    private int lcm(int a, int b) {
        if (a == 0 || b == 0)
            return 0;
        return Math.abs(a * b) / gcd(a, b);
    }

    public int calculateHcf(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty())
            return 0;
        int result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = gcd(result, numbers.get(i));
        }
        return result;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // AI Integration
    public String getAiResponse(String question) {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid question");
        }

        if (question.length() > 500) {
            throw new IllegalArgumentException("AI question too long (max 500 characters)");
        }

        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("API Key not configured");
        }

        try {
            // Enhanced prompt to ensure single word or short phrase answer
            String enhancedPrompt = "Answer the following question with ONLY a single word or short phrase (maximum 3 words). Do not provide explanations, sentences, or additional context. Just the direct answer:\n\n"
                    + question;

            Map<String, Object> requestBody = new HashMap<>();

            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();

            List<Map<String, String>> parts = new ArrayList<>();
            Map<String, String> part = new HashMap<>();
            part.put("text", enhancedPrompt);
            parts.add(part);

            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);

            // Generation config to limit output tokens
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", 0.1);
            generationConfig.put("maxOutputTokens", 50);
            requestBody.put("generationConfig", generationConfig);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String url = apiUrl + "?key=" + apiKey;

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode candidates = root.path("candidates");
                if (candidates.isArray() && candidates.size() > 0) {
                    String answer = candidates.get(0).path("content").path("parts").get(0).path("text").asText().trim();
                    // Clean up the answer
                    String[] words = answer.split("\\s+");
                    if (words.length > 3) {
                        return words[0];
                    }
                    return answer;
                } else {
                    throw new RuntimeException("No response candidate from AI");
                }
            } else {
                throw new RuntimeException("Error from AI API: " + response.getStatusCode());
            }

        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("Error calling AI Service: " + e.getMessage(), e);
        }
    }

}
