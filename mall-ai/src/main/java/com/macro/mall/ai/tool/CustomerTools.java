package com.macro.mall.ai.tool;

import com.macro.mall.mapper.OmsOrderMapper;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.OmsOrderExample;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.SmsCoupon;
import com.macro.mall.portal.service.PmsPortalProductService;
import com.macro.mall.portal.service.UmsMemberCouponService;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CustomerTools {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private PmsPortalProductService portalProductService;

    @Autowired
    private UmsMemberCouponService memberCouponService;

    @Tool("根据订单号查询订单状态，返回订单状态描述")
    public String getOrderStatus(Long userId, String orderSn) {
        try {
            OmsOrderExample example = new OmsOrderExample();
            example.createCriteria().andOrderSnEqualTo(orderSn).andMemberIdEqualTo(userId);
            List<OmsOrder> orders = orderMapper.selectByExample(example);
            if (orders == null || orders.isEmpty()) {
                return "未找到订单号 " + orderSn + " 的订单，请确认订单号是否正确。";
            }
            OmsOrder order = orders.get(0);
            String statusText = switch (order.getStatus()) {
                case 0 -> "待付款";
                case 1 -> "待发货";
                case 2 -> "已发货";
                case 3 -> "已完成";
                case 4 -> "已关闭";
                case 5 -> "无效订单";
                default -> "未知状态";
            };
            StringBuilder sb = new StringBuilder();
            sb.append("订单号：").append(orderSn).append("\n");
            sb.append("订单状态：").append(statusText).append("\n");
            sb.append("订单金额：¥").append(order.getTotalAmount()).append("\n");
            sb.append("下单时间：").append(order.getCreateTime());
            if (order.getStatus() == 2 && order.getDeliveryCompany() != null) {
                sb.append("\n物流公司：").append(order.getDeliveryCompany());
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("查询订单状态失败: orderSn={}, userId={}", orderSn, userId, e);
            return "查询订单状态失败，请稍后重试。";
        }
    }

    @Tool("根据订单号查询物流信息")
    public String getLogistics(Long userId, String orderSn) {
        try {
            OmsOrderExample example = new OmsOrderExample();
            example.createCriteria().andOrderSnEqualTo(orderSn).andMemberIdEqualTo(userId);
            List<OmsOrder> orders = orderMapper.selectByExample(example);
            if (orders == null || orders.isEmpty()) {
                return "未找到订单号 " + orderSn + " 的物流信息。";
            }
            OmsOrder order = orders.get(0);
            if (order.getStatus() < 2) {
                return "订单 " + orderSn + " 尚未发货，暂无物流信息。";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("订单号：").append(orderSn).append("\n");
            sb.append("物流公司：").append(order.getDeliveryCompany() != null ? order.getDeliveryCompany() : "未知")
                    .append("\n");
            sb.append("物流单号：").append(order.getDeliverySn() != null ? order.getDeliverySn() : "暂无").append("\n");
            sb.append("收货地址：").append(order.getReceiverProvince())
                    .append(order.getReceiverCity())
                    .append(order.getReceiverRegion())
                    .append(order.getReceiverDetailAddress()).append("\n");
            sb.append("收货人：").append(order.getReceiverName())
                    .append(" ").append(order.getReceiverPhone());
            return sb.toString();
        } catch (Exception e) {
            log.error("查询物流信息失败: orderSn={}, userId={}", orderSn, userId, e);
            return "查询物流信息失败，请稍后重试。";
        }
    }

    @Tool("查询用户可用优惠券")
    public String getCoupons(Long userId) {
        try {
            List<SmsCoupon> coupons = memberCouponService.list(0);
            if (coupons == null || coupons.isEmpty()) {
                return "您当前没有可用优惠券。";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("您有 ").append(coupons.size()).append(" 张可用优惠券：\n");
            for (int i = 0; i < coupons.size(); i++) {
                SmsCoupon c = coupons.get(i);
                sb.append(i + 1).append(". ").append(c.getName());
                if (c.getAmount() != null) {
                    sb.append("（满").append(c.getMinPoint()).append("减").append(c.getAmount()).append("）");
                }
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("查询优惠券失败: userId={}", userId, e);
            return "查询优惠券失败，请稍后重试。";
        }
    }

    @Tool("根据关键词搜索商品")
    public String searchProducts(String keyword) {
        try {
            List<PmsProduct> products = portalProductService.search(keyword, null, null, 1, 5, 1);
            if (products == null || products.isEmpty()) {
                return "未找到与 \"" + keyword + "\" 相关的商品。";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("为您找到 ").append(products.size()).append(" 款相关商品：\n");
            for (int i = 0; i < products.size(); i++) {
                PmsProduct p = products.get(i);
                sb.append(i + 1).append(". ").append(p.getName());
                sb.append(" - ¥").append(p.getPrice());
                sb.append("（").append(p.getSubTitle() != null ? p.getSubTitle() : "暂无描述").append("）\n");
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("搜索商品失败: keyword={}", keyword, e);
            return "搜索商品失败，请稍后重试。";
        }
    }

    @Tool("查询退换货规则")
    public String getRefundPolicy(String category) {
        return "【退换货政策】\n"
                + "1. 支持7天无理由退货（不影响二次销售）\n"
                + "2. 质量问题30天内包退包换\n"
                + "3. 生鲜/定制类商品不支持无理由退货\n"
                + "4. 退款将在3-7个工作日内原路返回\n"
                + "类目[" + category + "]具体规则请以商品详情页为准。";
    }
}
