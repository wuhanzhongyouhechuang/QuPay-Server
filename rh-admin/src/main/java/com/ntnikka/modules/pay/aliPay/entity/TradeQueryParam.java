package com.ntnikka.modules.pay.aliPay.entity;

import java.io.Serializable;

/**
 * @ClassName TradeQueryParam
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/25 10:09
 **/
public class TradeQueryParam implements Serializable {
    private String orderId;

    private String sign;

    private Long merchantId;

    private String partner;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
}
