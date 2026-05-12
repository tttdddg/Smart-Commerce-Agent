package com.macro.mall.common.dto.seckill;

import lombok.Data;

@Data
public class SeckillOrderData {
    private String requestId;
    private String status; // PROCESSING/SUCCESS/FAIL
}