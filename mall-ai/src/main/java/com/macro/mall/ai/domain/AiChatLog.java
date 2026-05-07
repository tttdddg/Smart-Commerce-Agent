package com.macro.mall.ai.domain;

import lombok.Data;

@Data
public class AiChatLog {
    private Long id;
    private String sessionId;
    private Long userId;
    private String agentType;
    private String question;
    private String answer;
    private String toolCallsJson;
    private Long latencyMs;
    private Integer status;
    private String errorMsg;
}