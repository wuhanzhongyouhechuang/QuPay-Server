package com.ntnikka.modules.pay.aliPay.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.modules.pay.aliPay.dao.TradePrecreateMsgDao;
import com.ntnikka.modules.pay.aliPay.entity.TradePrecreateMsg;
import com.ntnikka.modules.pay.aliPay.service.TradePrecreateMsgService;
import org.springframework.stereotype.Service;

/**
 * @ClassName TradePrecreateMsgServiceImpl
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/17 14:05
 **/
@Service("TradePrecreateMsgService")
public class TradePrecreateMsgServiceImpl extends ServiceImpl<TradePrecreateMsgDao, TradePrecreateMsg> implements TradePrecreateMsgService {
    @Override
    public void save(TradePrecreateMsg tradePrecreateMsg) {
        this.insert(tradePrecreateMsg);
    }
}
