package com.macro.mall.ai.controller;

import com.macro.mall.ai.service.AiChatService;
import com.macro.mall.common.api.CommonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai/chat")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/customer")
    public CommonResult<AiChatResp> customerChat(@Validated @RequestBody AiChatReq req) {
        return CommonResult.success(aiChatService.customerChat(req));
    }
}