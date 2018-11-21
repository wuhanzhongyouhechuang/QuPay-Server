package com.ntnikka.modules.pay.aliPay.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ntnikka.modules.pay.aliPay.entity.AliOrderEntity;
import com.ntnikka.modules.pay.aliPay.entity.TradePrecreateMsg;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by liuq on 2018/9/11.
 */
@Repository
public interface AliOrderDao extends BaseMapper<AliOrderEntity> {
    int checkRepeatId(String orderId);

    void saveTradePrecreateMsg(TradePrecreateMsg tradePrecreateMsg);

    AliOrderEntity queryByTradeId(String orderId);

    void updateTradeStatus(Map<String, Object> map);

    void updateTradeStatusClosed(String orderId);

    void updateNotifyStatus(String orderId);

    AliOrderEntity queryBySysTradeNo(String sysTradeNo);
}
