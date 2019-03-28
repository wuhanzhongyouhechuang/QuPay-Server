package com.ntnikka.modules.merchantManager.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * @ClassName ChannelEntity
 * @Author Liuq
 * @Description todo
 * @Date 2018/12/3 14:52
 **/
@TableName("merchant_channel")
public class ChannelEntity implements Serializable {

    private static final long serialVersionUID = -4265960008842949560L;

    @TableId
    private Long id;
    /**
     * 通道url
     */
    private String url;
    /**
     * 开关标志: 0-开启 1-关闭
     */
    private int flag;
    /**
     * 通道权重
     */
    private int weight;
    /**
     * 归属商户ID
     */
    private Long merchantId;
    /**
     * 删除标志 0-正常 ，-1-删除
     */
    private int delFlag;
    /**
     * 设备编号
     */
    private String deviceCode;

    private String aliUserId;

    /**
     * 账户名
     */
    private String bankAccount;
    /**
     * 银行代号
     */
    private String bankCode;
    /**
     * 银行卡号
     */
    private String bankCardNum;

    private String aliAccount;

    /**
     * 客户端websocketId
     */
    private String websocketId;

    public String getWebsocketId() {
        return websocketId;
    }

    public void setWebsocketId(String websocketId) {
        this.websocketId = websocketId;
    }

    public String getAliAccount() {
        return aliAccount;
    }

    public void setAliAccount(String aliAccount) {
        this.aliAccount = aliAccount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getAliUserId() {
        return aliUserId;
    }

    public void setAliUserId(String aliUserId) {
        this.aliUserId = aliUserId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }
}
