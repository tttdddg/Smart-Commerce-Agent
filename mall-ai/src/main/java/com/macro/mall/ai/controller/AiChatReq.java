package com.macro.mall.ai.controller;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AiChatReq {
    @NotBlank
    private String sessionId;
    @NotNull
    private Long userId;
    @NotBlank
    private String question;
}
