package com.macro.mall.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macro.mall.ai.config.CustomerAssistant;
import com.macro.mall.ai.controller.AiChatReq;
import com.macro.mall.ai.controller.AiChatResp;
import com.macro.mall.ai.dao.AiChatLogMapper;
import com.macro.mall.ai.domain.AiChatLog;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

    private final CustomerAssistant customerAssistant;
    private final AiChatLogMapper logMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiChatService(CustomerAssistant customerAssistant, AiChatLogMapper logMapper) {
        this.customerAssistant = customerAssistant;
        this.logMapper = logMapper;
    }

    public AiChatResp customerChat(AiChatReq req) {
        long start = System.currentTimeMillis();
        AiChatLog log = new AiChatLog();
        log.setSessionId(req.getSessionId());
        log.setUserId(req.getUserId());
        log.setAgentType("customer");
        log.setQuestion(req.getQuestion());

        try {
            String answer = customerAssistant.chat("userId=" + req.getUserId() + " question=" + req.getQuestion());
            long latency = System.currentTimeMillis() - start;

            log.setAnswer(answer);
            log.setLatencyMs(latency);
            log.setStatus(1);
            logMapper.insert(log);

            return AiChatResp.builder().answer(answer).latencyMs(latency).build();
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - start;
            log.setLatencyMs(latency);
            log.setStatus(0);
            log.setErrorMsg(e.getMessage());
            logMapper.insert(log);
            throw e;
        }
    }
}