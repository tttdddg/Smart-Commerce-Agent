package com.macro.mall.ai.controller;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class AiChatReq {
    @NotBlank
    private String sessionId;
    @NotNull
    private Long userId;
    @NotBlank
    private String question;
}