
package com.api.Projeto_3.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

@Configuration
public class OllamaConfig {

    @Value("${app.ollama.base-url:http://localhost:11434}")
    private String baseUrl;

    @Value("${app.ollama.model-name:llama3.2}")
    private String modelName;

    @Value("${app.ollama.timeout-seconds:180}")
    private long timeoutSeconds;

    @Bean
    @Primary
    public ChatModel chatModel() {

        return OllamaChatModel.builder()
                .httpClientBuilder(JdkHttpClient.builder())
                .baseUrl(baseUrl)
                .modelName(modelName)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .maxRetries(0)
                .temperature(0.2)
                .build();
    }
}
