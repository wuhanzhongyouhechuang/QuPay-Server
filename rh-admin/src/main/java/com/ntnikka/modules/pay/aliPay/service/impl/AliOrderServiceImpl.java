package com.ntnikka.modules.pay.aliPay.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.modules.pay.aliPay.dao.AliOrderDao;
import com.ntnikka.modules.pay.aliPay.entity.AliOrderEntity;
import com.ntnikka.modules.pay.aliPay.service.AliOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by liuq on 2018/9/11.
 */
@Service("AliOrderService")
public class AliOrderServiceImpl extends ServiceImpl<AliOrderDao, AliOrderEntity> implements AliOrderService {

    @Autowired
    private AliOrderDao aliOrderDao;

    @Override
    public void save(AliOrderEntity aliOrderEntity) {
        this.insert(aliOrderEntity);
    }

    @Override
    public int checkRepeatId(String orderId) {
        return aliOrderDao.checkRepeatId(orderId);
    }

    @Override
    public AliOrderEntity queryTradeId(String orderId) {
        return aliOrderDao.queryByTradeId(orderId);
    }

    @Override
    public void updateTradeOrder(Map<String, Object> map) {
        aliOrderDao.updateTradeStatus(map);
    }

    @Override
    public void updateTradeStatusClosed(String orderId) {
        aliOrderDao.updateTradeStatusClosed(orderId);
    }

    @Override
    public void updateNotifyStatus(String orderId) {
        aliOrderDao.updateNotifyStatus(orderId);
    }

    @Override
    public AliOrderEntity queryById(Long id) {
        return this.selectOne(new EntityWrapper<AliOrderEntity>().eq("id", id));
    }

    @Override
    public AliOrderEntity queryBySysTradeNo(String sysTradeNo) {
        return aliOrderDao.queryBySysTradeNo(sysTradeNo);
    }
}
