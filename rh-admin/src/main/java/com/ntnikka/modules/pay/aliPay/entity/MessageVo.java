package com.ntnikka.modules.pay.aliPay.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName MessageVo
 * @Author Liuq
 * @Description todo
 * @Date 2019/3/12 11:27
 **/
public class MessageVo implements Serializable {
    private static final long serialVersionUID = -45643412321L;

    private String orderNo ;
    private String userId ;
    private String price ;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
