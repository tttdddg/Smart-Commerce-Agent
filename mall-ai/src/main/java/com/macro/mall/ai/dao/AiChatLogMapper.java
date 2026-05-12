package com.macro.mall.ai.dao;

import com.macro.mall.ai.domain.AiChatLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AiChatLogMapper {
    int insert(AiChatLog log);

    List<AiChatLog> selectBySessionId(@Param("sessionId") String sessionId);

    List<AiChatLog> selectByUserId(@Param("userId") Long userId);
}
