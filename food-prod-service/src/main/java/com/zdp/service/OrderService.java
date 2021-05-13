package com.zdp.service;

import com.zdp.pojo.OrderStatus;
import com.zdp.pojo.bo.SubmitOrderBO;
import com.zdp.pojo.vo.OrderVO;

/**
 * @author sesshomaru
 * @date 2021/5/11 21:43
 */
public interface OrderService {

    // 创建订单
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    public OrderStatus queryOrderStatusInfo(String orderId);
}
