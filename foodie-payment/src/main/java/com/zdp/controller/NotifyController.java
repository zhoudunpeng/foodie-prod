package com.zdp.controller;

import com.zdp.service.PaymentOrderService;
import com.zdp.utils.DateUtil;
import com.zdp.wx.entity.PayResult;
import com.zdp.wx.service.WxOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


/**
 * @author sesshomaru
 * @date 2021/5/14
 */
@RestController
@RequestMapping(value = "/payment/notice")
public class NotifyController {

    final static Logger log = LoggerFactory.getLogger(NotifyController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WxOrderService wxOrderService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    /**
     * 支付成功后的微信支付异步通知
     */
    @RequestMapping(value="/wxpay")
    public void wxpay(HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.info("支付成功后的微信支付异步通知");

        // 获取微信支付结果
        PayResult payResult = wxOrderService.getWxPayResult(request.getInputStream());

        boolean isPaid = payResult.getReturn_code().equals("SUCCESS") ? true : false;
        // 查询该笔订单在微信那边是否成功支付
        // 支付成功，商户处理后同步返回给微信参数
        PrintWriter writer = response.getWriter();
        if (isPaid) {

            String merchantOrderId = payResult.getOut_trade_no();			// 商户订单号
            String wxFlowId = payResult.getTransaction_id();
            Integer paidAmount = payResult.getTotal_fee();

//			System.out.println("================================= 支付成功 =================================");

            // ====================== 操作商户自己的业务，比如修改订单状态等 start ==========================
            String merchantReturnUrl = paymentOrderService.updateOrderPaid(merchantOrderId, paidAmount);
            // ============================================ 业务结束， end ==================================

            log.info("************* 支付成功(微信支付异步通知) - 时间: {} *************", DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
            log.info("* 商户订单号: {}", merchantOrderId);
            log.info("* 微信订单号: {}", wxFlowId);
            log.info("* 实际支付金额: {}", paidAmount);
            log.info("*****************************************************************************");


            // 通知天天吃货服务端订单已支付
//			String url = "http://192.168.1.2:8088/orders/notifyMerchantOrderPaid";

            MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
            requestEntity.add("merchantOrderId", merchantOrderId);
            String httpStatus = restTemplate.postForObject(merchantReturnUrl, requestEntity, String.class);
            log.info("*** 通知天天吃货后返回的状态码 httpStatus: {} ***", httpStatus);

            // 通知微信已经收到消息，不要再给我发消息了，否则微信会10连击调用本接口
            String noticeStr = setXML("SUCCESS", "");
            writer.write(noticeStr);
            writer.flush();

        } else {
            System.out.println("================================= 支付失败 =================================");

            // 支付失败
            String noticeStr = setXML("FAIL", "");
            writer.write(noticeStr);
            writer.flush();
        }

    }

    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }
}


