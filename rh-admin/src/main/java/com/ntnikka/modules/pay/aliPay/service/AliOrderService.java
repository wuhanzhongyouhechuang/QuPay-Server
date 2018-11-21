package com.ntnikka.modules.pay.aliPay.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.pay.aliPay.entity.AliOrderEntity;
import com.ntnikka.modules.pay.aliPay.entity.TradePrecreateMsg;

import java.util.Map;

/**
 * Created by liuq on 2018/9/11.
 */
public interface AliOrderService extends IService<AliOrderEntity> {
    void save(AliOrderEntity aliOrderEntity);

    int checkRepeatId(String orderId);

    AliOrderEntity queryTradeId(String orderId);

    void updateTradeOrder(Map<String, Object> map);

    void updateTradeStatusClosed(String orderId);

    void updateNotifyStatus(String orderId);

    AliOrderEntity queryById(Long id);

    AliOrderEntity queryBySysTradeNo(String sysTradeNo);
}
