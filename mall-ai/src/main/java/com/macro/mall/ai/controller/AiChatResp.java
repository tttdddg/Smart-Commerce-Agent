package com.macro.mall.ai.controller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiChatResp {
    private String answer;
    private Long latencyMs;
}