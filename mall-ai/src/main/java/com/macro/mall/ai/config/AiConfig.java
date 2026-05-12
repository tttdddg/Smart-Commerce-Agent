package com.macro.mall.ai.config;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AiConfig {

    @Bean
    OpenAiChatModel openAiChatModel(
            @Value("${ai.model.base-url}") String baseUrl,
            @Value("${ai.model.api-key}") String apiKey,
            @Value("${ai.model.model-name}") String modelName,
            @Value("${ai.model.timeout-seconds}") Integer timeoutSeconds) {
        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }
}
