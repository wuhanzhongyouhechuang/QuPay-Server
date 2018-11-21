package com.ntnikka.modules.merchantManager.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Liuq
 * @email 530775870@qq.com
 * @date 2018-10-10 11:56:51
 */
@TableName("app_id")
public class AppIdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 渠道商id
     */
    private String appid;
    /**
     * 支付宝公钥
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
     *
     */
    @TableId
    private Integer id;

    /**
     * 设置：渠道商id
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * 获取：渠道商id
     */
    public String getAppid() {
        return appid;
    }

    /**
     * 设置：支付宝公钥
     */
    public void setAliPubKey(String aliPubKey) {
        this.aliPubKey = aliPubKey;
    }

    /**
     * 获取：支付宝公钥
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
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }
}
