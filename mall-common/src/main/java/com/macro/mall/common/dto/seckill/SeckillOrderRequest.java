package com.macro.mall.common.dto.seckill;

import lombok.Data;

@Data
public class SeckillOrderRequest {
    private Long activityId;
    private Long skuId;
    private Long memberId;
}
