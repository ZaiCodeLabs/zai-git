package za.co.zaicodelabs.zaigit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class OllamaService {

    private final ConfigService configService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OllamaService(ConfigService configService) {
        this.configService = configService;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public boolean isAvailable() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(endpoint("/api/tags"))
                    .GET()
                    .timeout(Duration.ofSeconds(5))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateCommitMessage(String diff) {
        if (!isAvailable()) {
            return "chore: update files";
        }

        try {
            String prompt = """
                Generate a concise git commit message following conventional commits format.
                Use one of these prefixes: feat, fix, chore, docs, style, refactor, test, perf.
                
                Example formats:
                - feat: add user authentication
                - fix: resolve null pointer in login
                - chore: update dependencies
                
                Keep it under 50 characters. Be specific but brief.
                
                Changes:
                %s
                
                Respond with ONLY the commit message, nothing else.
                """.formatted(diff);

            String requestBody = objectMapper.writeValueAsString(
                    new OllamaRequest(configService.getOllamaModel(), prompt, false)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(endpoint("/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body();

                // Handle streaming response - each line is a JSON object
                StringBuilder fullResponse = new StringBuilder();
                String[] lines = body.split("\n");

                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;

                    try {
                        JsonNode jsonNode = objectMapper.readTree(line);
                        if (jsonNode.has("response")) {
                            fullResponse.append(jsonNode.get("response").asText());
                        }
                    } catch (Exception e) {
                        // Skip malformed lines
                    }
                }

                String generatedText = fullResponse.toString().trim();

                // Clean up the response
                generatedText = generatedText.replaceAll("```.*", "").trim();
                generatedText = generatedText.replaceAll("^['\"`]|['\"`]$", "").trim(); // Remove quotes

                // Take first line only
                String[] messageLines = generatedText.split("\n");
                if (messageLines.length > 0) {
                    generatedText = messageLines[0].trim();
                }

                // Validate it looks like a commit message
                if (generatedText.isEmpty() || generatedText.length() > 100) {
                    return "chore: update files";
                }

                // Ensure it has a conventional commit prefix
                if (!generatedText.matches("^(feat|fix|chore|docs|style|refactor|test|perf|build|ci|revert).*")) {
                    // Try to add one if missing
                    if (diff.toLowerCase().contains("add") || diff.toLowerCase().contains("new")) {
                        generatedText = "feat: " + generatedText;
                    } else if (diff.toLowerCase().contains("fix") || diff.toLowerCase().contains("bug")) {
                        generatedText = "fix: " + generatedText;
                    } else {
                        generatedText = "chore: " + generatedText;
                    }
                }

                return generatedText;
            }
        } catch (Exception e) {
            System.err.println("Ollama error: " + e.getMessage());
        }

        return "chore: update files";
    }

    public String explainConflict(String conflictContent) {
        if (!isAvailable()) {
            return "Please resolve the conflict manually.";
        }

        try {
            String prompt = """
                Explain this git merge conflict in simple, clear terms.
                Provide:
                1. What caused the conflict
                2. What the differences are
                3. How to resolve it (step by step)
                
                Keep your explanation concise and actionable.
                
                Conflict:
                %s
                """.formatted(conflictContent);

            String requestBody = objectMapper.writeValueAsString(
                    new OllamaRequest(configService.getOllamaModel(), prompt, false)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(endpoint("/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body();

                // Handle streaming response
                StringBuilder fullResponse = new StringBuilder();
                String[] lines = body.split("\n");

                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;

                    try {
                        JsonNode jsonNode = objectMapper.readTree(line);
                        if (jsonNode.has("response")) {
                            fullResponse.append(jsonNode.get("response").asText());
                        }
                    } catch (Exception e) {
                        // Skip malformed lines
                    }
                }

                return fullResponse.toString().trim();
            }
        } catch (Exception e) {
            System.err.println("Ollama error: " + e.getMessage());
        }

        return "Please resolve the conflict manually.";
    }

    // Inner class for Ollama API request
    private record OllamaRequest(String model, String prompt, boolean stream) {}

    private URI endpoint(String path) {
        String baseUrl = configService.getOllamaUrl().replaceAll("/+$", "");
        return URI.create(baseUrl + path);
    }
}
