package com.ntnikka.modules.merchantManager.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * @ClassName MerchantSettleChannel
 * @Author Liuq
 * @Description todo
 * @Date 2018/12/25 9:16
 **/
@TableName("merchant_settle_channel")
public class MerchantSettleChannel implements Serializable {

    private static final long serialVersionUID = 3183612654786162771L;

    @TableId
    private Integer id;

    private String aliUserId;

    private double amountPercent;

    private int flag;

    private int delFlag;

    private Long merchantId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAliUserId() {
        return aliUserId;
    }

    public void setAliUserId(String aliUserId) {
        this.aliUserId = aliUserId;
    }

    public double getAmountPercent() {
        return amountPercent;
    }

    public void setAmountPercent(double amountPercent) {
        this.amountPercent = amountPercent;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
}
