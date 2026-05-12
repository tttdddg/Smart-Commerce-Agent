package com.macro.mall.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macro.mall.ai.config.CustomerAssistant;
import com.macro.mall.ai.config.ShopAssistant;
import com.macro.mall.ai.controller.AiChatReq;
import com.macro.mall.ai.controller.AiChatResp;
import com.macro.mall.ai.dao.AiChatLogMapper;
import com.macro.mall.ai.domain.AiChatLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AiChatService {

    @Autowired
    private AiSessionService aiSessionService;

    @Autowired
    private AiChatLogMapper logMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiChatResp customerChat(AiChatReq req) {
        return doChat(req, "customer");
    }

    public AiChatResp shopChat(AiChatReq req) {
        return doChat(req, "shop");
    }

    private AiChatResp doChat(AiChatReq req, String agentType) {
        long start = System.currentTimeMillis();
        AiChatLog chatLog = new AiChatLog();
        chatLog.setSessionId(req.getSessionId());
        chatLog.setUserId(req.getUserId());
        chatLog.setAgentType(agentType);
        chatLog.setQuestion(req.getQuestion());

        try {
            String answer;
            String userMessage = buildUserMessage(req);

            if ("shop".equals(agentType)) {
                ShopAssistant assistant = aiSessionService.getShopAssistant(req.getSessionId());
                answer = assistant.chat(userMessage);
            } else {
                CustomerAssistant assistant = aiSessionService.getCustomerAssistant(req.getSessionId());
                answer = assistant.chat(userMessage);
            }

            long latency = System.currentTimeMillis() - start;
            chatLog.setAnswer(answer);
            chatLog.setLatencyMs(latency);
            chatLog.setStatus(1);
            logMapper.insert(chatLog);

            return AiChatResp.builder().answer(answer).latencyMs(latency).build();
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - start;
            chatLog.setLatencyMs(latency);
            chatLog.setStatus(0);
            chatLog.setErrorMsg(e.getMessage());
            logMapper.insert(chatLog);
            log.error("AI处理失败: agentType={}, sessionId={}, userId={}", agentType, req.getSessionId(), req.getUserId(), e);
            return AiChatResp.builder()
                    .answer("抱歉，系统暂时无法处理您的请求，请稍后重试或联系人工客服。")
                    .latencyMs(latency)
                    .build();
        }
    }

    private String buildUserMessage(AiChatReq req) {
        return "userId=" + req.getUserId() + " question=" + req.getQuestion();
    }
}
