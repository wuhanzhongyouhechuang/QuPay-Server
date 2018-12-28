package com.ntnikka.modules.merchantManager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.modules.merchantManager.dao.MerchantSettleDao;
import com.ntnikka.modules.merchantManager.entity.MerchantSettleChannel;
import com.ntnikka.modules.merchantManager.service.MerchantSettleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MerchantSettleServiceImpl
 * @Author Liuq
 * @Description todo
 * @Date 2018/12/25 11:04
 **/
@Service("MerchantSettleService")
public class MerchantSettleServiceImpl extends ServiceImpl<MerchantSettleDao , MerchantSettleChannel> implements MerchantSettleService {

    @Autowired
    MerchantSettleDao merchantSettleDao;

    @Override
    public List<MerchantSettleChannel> queryByMerchantId(Long merchantId) {
        return this.selectList( new EntityWrapper<MerchantSettleChannel>().eq("merchant_id" , merchantId)
                .eq("del_flag",0)
                .eq("flag",0));
    }

    @Override
    public void saveOrUpdateSettleChannel(List<MerchantSettleChannel> merchantSettleChannelList) {
        this.insertOrUpdateBatch(merchantSettleChannelList);
    }

    @Override
    public List<MerchantSettleChannel> queryUseableSettleList(Long merchantId) {
        return this.selectList( new EntityWrapper<MerchantSettleChannel>().eq("merchant_id" , merchantId)
                .eq("del_flag",0)
                .eq("flag",0));
    }

    @Override
    public void updateSettleChannel(Map map) {
        merchantSettleDao.updateSettleChannel(map);
    }

    @Override
    public List<MerchantSettleChannel> querySettleList(Long merchantId) {
        return this.selectList( new EntityWrapper<MerchantSettleChannel>().eq("merchant_id" , merchantId)
                .eq("del_flag",0));
    }

    @Override
    public void closeSettleChannel(Integer settleChannelId) {
        merchantSettleDao.closeSettleChannel(settleChannelId);
    }
}
