package com.macro.mall.ai.tool;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class CustomerTools {

    @Tool("根据订单号查询订单状态，返回已支付、待发货、已发货、已完成等")
    public String getOrderStatus(Long userId, String orderSn) {
        // TODO: 接入 mall-portal 的 OmsPortalOrderService
        return "订单" + orderSn + "当前状态：已发货，预计明天送达。";
    }

    @Tool("根据订单号查询物流信息")
    public String getLogistics(Long userId, String orderSn) {
        // TODO: 接三方物流或内部物流表
        return "物流：上海分拨中心 -> 杭州转运中心 -> 派送中。";
    }

    @Tool("查询用户可用优惠券")
    public String getCoupons(Long userId) {
        // TODO: 接 UmsMemberCouponService
        return "你有2张可用优惠券：满199减20、满299减40。";
    }

    @Tool("查询退换货规则")
    public String getRefundPolicy(String category) {
        return "类目[" + category + "]支持7天无理由退货（不影响二次销售）。";
    }
}