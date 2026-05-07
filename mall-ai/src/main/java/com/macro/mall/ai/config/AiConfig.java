package com.macro.mall.ai.config;

import com.macro.mall.ai.tool.CustomerTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

public interface CustomerAssistant {
    @SystemMessage("""
你是电商客服助手：
1) 只回答电商相关问题；
2) 涉及订单/物流/优惠券，优先调用工具查询真实数据；
3) 无法确认时明确说“不确定”，并给下一步建议；
4) 不执行高风险操作（退款、取消订单），只给操作指引。
""")
    String chat(String userMessage);
}

@Configuration
class AiConfig {

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

    @Bean
    CustomerAssistant customerAssistant(OpenAiChatModel model, CustomerTools tools,
                                        @Value("${ai.memory.max-messages}") Integer maxMessages) {
        return AiServices.builder(CustomerAssistant.class)
                .chatModel(model)
                .tools(tools)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(maxMessages))
                .build();
    }
}