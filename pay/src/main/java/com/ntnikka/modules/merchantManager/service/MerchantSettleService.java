package com.ntnikka.modules.merchantManager.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.merchantManager.entity.MerchantSettleChannel;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MerchantSettleService
 * @Author Liuq
 * @Description todo
 * @Date 2018/12/25 11:03
 **/
public interface MerchantSettleService extends IService<MerchantSettleChannel> {

    List<MerchantSettleChannel> queryByMerchantId(Long merchantId);

    void saveOrUpdateSettleChannel(List<MerchantSettleChannel> merchantSettleChannelList);

    List<MerchantSettleChannel> queryUseableSettleList(Long merchantId);

    List<MerchantSettleChannel> querySettleList(Long merchantId);

    void updateSettleChannel(Map map);

    void closeSettleChannel(Integer settleChannelId);
}
