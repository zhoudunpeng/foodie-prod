package com.zdp.service;

import com.zdp.pojo.bo.SubmitOrderBO;
import com.zdp.pojo.vo.OrderVO;

/**
 * @author sesshomaru
 * @date 2021/5/11 21:43
 */
public interface OrderService {

    // 创建订单
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);
}
