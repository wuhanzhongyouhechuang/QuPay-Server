package com.ntnikka.modules.merchantManager.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Liuq
 * @email
 * @date 2018-09-15 16:41:11
 */
@TableName("merchant")
public class MerchantEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 商户联系方式
     */
    private String merchantPhone;
    /**
     * 商户密钥
     */
    private String merchantKey;
    /**
     * 商户状态:0通过(暂时注册就通过默认0)
     */
    private Integer status;
    /**
     * 商户交易权限(注册默认开启)0-开启 1-关闭
     */
    private Integer tradeStatus;
    /**
     * 商户Pid
     */
    private Long pid;
    /**
     * 门店编号
     */
    private String storeNumber;
    /**
     *
     */
    private String authCode;
    /**
     * 渠道商appid
     */
    private Long appid;
    /**
     * 渠道商支付宝公钥
     */
    private String aliPubKey;
    /**
     * 渠道商开发公钥
     */
    private String merchantPubKey;
    /**
     * 渠道商开发私钥
     */
    private String merchantPriKey;
    /**
     * 上级商户id
     */
    private Long merchantDeptId;

    /**
     * 上级合作商名称
     */
    private String merchantDeptName;

    /**
     * 个人码绑定手机外网地址
     */
    private String mobileUrl;

    private String wechatNum;

    private String wechatKey;

    private String settleId;

    private Integer settleFlag;

    private String settleIdOut;

    private int priFlag;

    @TableField(exist = false)
    private List<ChannelEntity> channelList;

    private int pollingFlag;

    public int getPollingFlag() {
        return pollingFlag;
    }

    public void setPollingFlag(int pollingFlag) {
        this.pollingFlag = pollingFlag;
    }

    public List<ChannelEntity> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelEntity> channelList) {
        this.channelList = channelList;
    }

    public int getPriFlag() {
        return priFlag;
    }

    public void setPriFlag(int priFlag) {
        this.priFlag = priFlag;
    }

    public String getSettleIdOut() {
        return settleIdOut;
    }

    public void setSettleIdOut(String settleIdOut) {
        this.settleIdOut = settleIdOut;
    }

    public String getSettleId() {
        return settleId;
    }

    public void setSettleId(String settleId) {
        this.settleId = settleId;
    }

    public Integer getSettleFlag() {
        return settleFlag;
    }

    public void setSettleFlag(Integer settleFlag) {
        this.settleFlag = settleFlag;
    }

    public String getWechatNum() {
        return wechatNum;
    }

    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum;
    }

    public String getWechatKey() {
        return wechatKey;
    }

    public void setWechatKey(String wechatKey) {
        this.wechatKey = wechatKey;
    }

    public String getMobileUrl() {
        return mobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
    }

    public String getMerchantDeptName() {
        return merchantDeptName;
    }

    public void setMerchantDeptName(String merchantDeptName) {
        this.merchantDeptName = merchantDeptName;
    }

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：商户名称
     */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /**
     * 获取：商户名称
     */
    public String getMerchantName() {
        return merchantName;
    }

    /**
     * 设置：商户联系方式
     */
    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    /**
     * 获取：商户联系方式
     */
    public String getMerchantPhone() {
        return merchantPhone;
    }

    /**
     * 设置：商户密钥
     */
    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }

    /**
     * 获取：商户密钥
     */
    public String getMerchantKey() {
        return merchantKey;
    }

    /**
     * 设置：商户状态:0通过(暂时注册就通过默认0)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：商户状态:0通过(暂时注册就通过默认0)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置：商户交易权限(注册默认开启)0-开启 1-关闭
     */
    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    /**
     * 获取：商户交易权限(注册默认开启)0-开启 1-关闭
     */
    public Integer getTradeStatus() {
        return tradeStatus;
    }

    /**
     * 设置：商户Pid
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * 获取：商户Pid
     */
    public Long getPid() {
        return pid;
    }

    /**
     * 设置：门店编号
     */
    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    /**
     * 获取：门店编号
     */
    public String getStoreNumber() {
        return storeNumber;
    }

    /**
     * 设置：
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    /**
     * 获取：
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * 设置：渠道商appid
     */
    public void setAppid(Long appid) {
        this.appid = appid;
    }

    /**
     * 获取：渠道商appid
     */
    public Long getAppid() {
        return appid;
    }

    /**
     * 设置：渠道商支付宝公钥
     */
    public void setAliPubKey(String aliPubKey) {
        this.aliPubKey = aliPubKey;
    }

    /**
     * 获取：渠道商支付宝公钥
     */
    public String getAliPubKey() {
        return aliPubKey;
    }

    /**
     * 设置：渠道商开发公钥
     */
    public void setMerchantPubKey(String merchantPubKey) {
        this.merchantPubKey = merchantPubKey;
    }

    /**
     * 获取：渠道商开发公钥
     */
    public String getMerchantPubKey() {
        return merchantPubKey;
    }

    /**
     * 设置：渠道商开发私钥
     */
    public void setMerchantPriKey(String merchantPriKey) {
        this.merchantPriKey = merchantPriKey;
    }

    /**
     * 获取：渠道商开发私钥
     */
    public String getMerchantPriKey() {
        return merchantPriKey;
    }

    /**
     * 设置：上级商户id
     */
    public void setMerchantDeptId(Long merchantDeptId) {
        this.merchantDeptId = merchantDeptId;
    }

    /**
     * 获取：上级商户id
     */
    public Long getMerchantDeptId() {
        return merchantDeptId;
    }
}
