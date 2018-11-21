package com.ntnikka.modules.pay.aliPay.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.pay.aliPay.entity.TradePrecreateMsg;

/**
 * @ClassName TradePrecreateMsgService
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/17 14:03
 **/
public interface TradePrecreateMsgService extends IService<TradePrecreateMsg> {
    void save(TradePrecreateMsg tradePrecreateMsg);
}
