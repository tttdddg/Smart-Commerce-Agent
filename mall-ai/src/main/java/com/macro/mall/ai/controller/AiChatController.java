package com.macro.mall.ai.controller;

import com.macro.mall.ai.service.AiChatService;
import com.macro.mall.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai/chat")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    @PostMapping("/customer")
    public CommonResult<AiChatResp> customerChat(@Validated @RequestBody AiChatReq req) {
        return CommonResult.success(aiChatService.customerChat(req));
    }

    @PostMapping("/shop")
    public CommonResult<AiChatResp> shopChat(@Validated @RequestBody AiChatReq req) {
        return CommonResult.success(aiChatService.shopChat(req));
    }
}
