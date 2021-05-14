package com.zdp.controller;

import com.zdp.config.RedisOperator;
import com.zdp.enums.PayMethod;
import com.zdp.enums.PaymentStatus;
import com.zdp.pojo.Orders;
import com.zdp.pojo.bo.MerchantOrdersBO;
import com.zdp.pojo.vo.PaymentInfoVO;
import com.zdp.resource.WXPayResource;
import com.zdp.service.PaymentOrderService;
import com.zdp.utils.IMOOCJSONResult;
import com.zdp.wx.entity.PreOrderResult;
import com.zdp.wx.service.WxOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sesshomaru
 * @date 2021/5/14
 */
@RestController
@RequestMapping(value = "payment")
public class PaymentController {

    final static Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    public RedisOperator redis;

    @Autowired
    private WXPayResource wxPayResource;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private WxOrderService wxOrderService;

    /**
     * 接受商户订单信息，保存到自己的数据库
     */
    @PostMapping("/createMerchantOrder")
    public IMOOCJSONResult createMerchantOrder(@RequestBody MerchantOrdersBO merchantOrdersBO, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String merchantOrderId = merchantOrdersBO.getMerchantOrderId();             	// 订单id
        String merchantUserId = merchantOrdersBO.getMerchantUserId();     		// 用户id
        Integer amount = merchantOrdersBO.getAmount();    // 实际支付订单金额
        Integer payMethod = merchantOrdersBO.getPayMethod();          	// 支付方式
        String returnUrl = merchantOrdersBO.getReturnUrl();           	// 支付成功后的回调地址（学生自定义）

        if (StringUtils.isBlank(merchantOrderId)) {
            return IMOOCJSONResult.errorMsg("参数[orderId]不能为空");
        }
        if (StringUtils.isBlank(merchantUserId)) {
            return IMOOCJSONResult.errorMsg("参数[userId]不能为空");
        }
        if (amount == null || amount < 1) {
            return IMOOCJSONResult.errorMsg("参数[realPayAmount]不能为空并且不能小于1");
        }
        if (payMethod == null) {
            return IMOOCJSONResult.errorMsg("参数[payMethod]不能为空并且不能小于1");
        }
        if (payMethod != PayMethod.WEIXIN.type && payMethod != PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg("参数[payMethod]目前只支持微信支付或支付宝支付");
        }
        if (StringUtils.isBlank(returnUrl)) {
            return IMOOCJSONResult.errorMsg("参数[returnUrl]不能为空");
        }

        // 保存传来的商户订单信息
        boolean isSuccess = false;
        try {
            isSuccess = paymentOrderService.createPaymentOrder(merchantOrdersBO);
        } catch (Exception e) {
            e.printStackTrace();
            IMOOCJSONResult.errorException(e.getMessage());
        }

        if (isSuccess) {
            return IMOOCJSONResult.ok("商户订单创建成功！");
        } else {
            return IMOOCJSONResult.errorMsg("商户订单创建失败，请重试...");
        }
    }


    /**
     * 提供给大家查询的方法，用于查询订单信息
     * @param merchantOrderId
     * @param merchantUserId
     * @return
     */
    @PostMapping("getPaymentCenterOrderInfo")
    public IMOOCJSONResult getPaymentCenterOrderInfo(String merchantOrderId, String merchantUserId) {

        if (StringUtils.isBlank(merchantOrderId) || StringUtils.isBlank(merchantUserId)) {
            return IMOOCJSONResult.errorMsg("查询参数不能为空！");
        }

        Orders orderInfo = paymentOrderService.queryOrderInfo(merchantUserId, merchantOrderId);

        return IMOOCJSONResult.ok(orderInfo);
    }



    /**
     * @Description: 微信扫码支付页面
     */
    //@GetMapping("/getWXPayQRCode")
    @PostMapping(value="/getWXPayQRCode")
    public IMOOCJSONResult getWXPayQRCode(String merchantOrderId, String merchantUserId) throws Exception{

//		System.out.println(wxPayResource.toString());

        // 根据订单ID和用户ID查询订单详情
        Orders waitPayOrder = paymentOrderService.queryOrderByStatus(merchantUserId, merchantOrderId, PaymentStatus.WAIT_PAY.type);

        // 商品描述
        String body = "天天吃货-付款用户[" + merchantUserId + "]";
        // 商户订单号
        String out_trade_no = merchantOrderId;
        // 从redis中去获得这笔订单的微信支付二维码，如果订单状态没有支付没有就放入，这样的做法防止用户频繁刷新而调用微信接口
        if (waitPayOrder != null) {
            String qrCodeUrl = redis.get(wxPayResource.getQrcodeKey() + ":" + merchantOrderId);

            if (StringUtils.isEmpty(qrCodeUrl)) {
                // 订单总金额，单位为分
                String total_fee = String.valueOf(waitPayOrder.getAmount());
//				String total_fee = "1";	// 测试用 1分钱

                // 统一下单
                PreOrderResult preOrderResult = wxOrderService.placeOrder(body, out_trade_no, total_fee);
                qrCodeUrl = preOrderResult.getCode_url();
            }

            PaymentInfoVO paymentInfoVO = new PaymentInfoVO();
            paymentInfoVO.setAmount(waitPayOrder.getAmount());
            paymentInfoVO.setMerchantOrderId(merchantOrderId);
            paymentInfoVO.setMerchantUserId(merchantUserId);
            paymentInfoVO.setQrCodeUrl(qrCodeUrl);

            redis.set(wxPayResource.getQrcodeKey() + ":" + merchantOrderId, qrCodeUrl, wxPayResource.getQrcodeExpire());

            return IMOOCJSONResult.ok(paymentInfoVO);
        } else {
            return IMOOCJSONResult.errorMsg("该订单不存在，或已经支付");
        }
    }
}
