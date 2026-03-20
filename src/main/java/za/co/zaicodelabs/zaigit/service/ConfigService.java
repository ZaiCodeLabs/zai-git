package za.co.zaicodelabs.zaigit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConfigService {

    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.zaigit";
    private static final String CONFIG_FILE = CONFIG_DIR + "/config.json";

    private final ObjectMapper objectMapper;
    private Map<String, String> config;

    public ConfigService() {
        this.objectMapper = new ObjectMapper();
        this.config = new HashMap<>();
        loadConfig();
    }

    private void loadConfig() {
        try {
            Path configPath = Paths.get(CONFIG_FILE);
            if (Files.exists(configPath)) {
                String json = Files.readString(configPath);
                config = objectMapper.readValue(json, Map.class);
            } else {
                // Set defaults
                config.put("ollama.url", "http://localhost:11434");
                config.put("ollama.model", "codellama");
                config.put("git.confirm-before-push", "true");
                config.put("git.auto-pull-before-push", "true");
                config.put("features.ai-commit-messages", "true");
                config.put("features.push-confirmation", "true");
            }
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
            setDefaults();
        }
    }

    private void setDefaults() {
        config = new HashMap<>();
        config.put("ollama.url", "http://localhost:11434");
        config.put("ollama.model", "codellama");
        config.put("git.confirm-before-push", "true");
        config.put("git.auto-pull-before-push", "true");
        config.put("features.ai-commit-messages", "true");
        config.put("features.push-confirmation", "true");
    }

    public void saveConfig() {
        try {
            // Create config directory if it doesn't exist
            Path configDir = Paths.get(CONFIG_DIR);
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }

            // Write config to file
            String json = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(config);
            Files.writeString(Paths.get(CONFIG_FILE), json);
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    public String get(String key) {
        return config.getOrDefault(key, "");
    }

    public String get(String key, String defaultValue) {
        return config.getOrDefault(key, defaultValue);
    }

    public void set(String key, String value) {
        config.put(key, value);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(config.getOrDefault(key, "false"));
    }

    public void setBoolean(String key, boolean value) {
        config.put(key, String.valueOf(value));
    }

    public Map<String, String> getAllConfig() {
        return new HashMap<>(config);
    }

    public String getOllamaUrl() {
        return get("ollama.url", "http://localhost:11434");
    }

    public void setOllamaUrl(String url) {
        set("ollama.url", url);
    }

    public String getOllamaModel() {
        return get("ollama.model", "codellama");
    }

    public void setOllamaModel(String model) {
        set("ollama.model", model);
    }

    public boolean isConfirmBeforePush() {
        return getBoolean("git.confirm-before-push");
    }

    public void setConfirmBeforePush(boolean confirm) {
        setBoolean("git.confirm-before-push", confirm);
    }

    public boolean isAutoPullBeforePush() {
        return getBoolean("git.auto-pull-before-push");
    }

    public void setAutoPullBeforePush(boolean autoPull) {
        setBoolean("git.auto-pull-before-push", autoPull);
    }

    public boolean isAiCommitMessagesEnabled() {
        return getBoolean("features.ai-commit-messages");
    }

    public void setAiCommitMessagesEnabled(boolean enabled) {
        setBoolean("features.ai-commit-messages", enabled);
    }

    public String getConfigFilePath() {
        return CONFIG_FILE;
    }
}