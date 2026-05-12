package com.macro.mall.ai.controller;

import com.macro.mall.ai.service.AiChatService;
import com.macro.mall.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "AI聊天")
@RestController
@RequestMapping("/ai/chat")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    @ApiOperation("客服对话")
    @PostMapping("/customer")
    public CommonResult<AiChatResp> customerChat(@Validated @RequestBody AiChatReq req) {
        return CommonResult.success(aiChatService.customerChat(req));
    }

    @ApiOperation("导购对话")
    @PostMapping("/shop")
    public CommonResult<AiChatResp> shopChat(@Validated @RequestBody AiChatReq req) {
        return CommonResult.success(aiChatService.shopChat(req));
    }
}
