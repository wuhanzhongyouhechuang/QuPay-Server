package com.ntnikka.modules.merchantManager.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.merchantManager.entity.MerchantEntity;
import com.ntnikka.utils.PageUtils;

import java.util.Map;

/**
 * @author Liuq
 * @email
 * @date 2018-09-18 16:41:11
 */
public interface MerchantService extends IService<MerchantEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateTradeStatus(Map<String, Object> params);

    void save(MerchantEntity merchantEntity);

    MerchantEntity findByPriKey(String priKey);

    MerchantEntity queryById(Long merchantId);

    void updateSettleStatus(Map<String, Object> params);

    void closeChannel(Long channelId);

    void closeTradeStatus(Long merchantId);

    void closeTradeFlag(Long merchantrId);
}

