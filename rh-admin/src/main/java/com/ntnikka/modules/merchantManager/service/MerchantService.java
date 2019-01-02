package com.ntnikka.modules.merchantManager.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.merchantManager.entity.ChannelEntity;
import com.ntnikka.modules.merchantManager.entity.MerchantEntity;
import com.ntnikka.modules.orderManager.entity.TradeOrder;
import com.ntnikka.utils.PageUtils;

import java.util.List;
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

    List<MerchantEntity> hasOrder();

    List<TradeOrder> queryYesterdayOrderList(Map<String, Object> params);

    PageUtils queryPageForPriMerchant(Map<String, Object> params);

    List<ChannelEntity> queryChannelList(Long merchantId);

    void updatePri(MerchantEntity merchantEntity);

    void updateChannelFlag(Map map);

    void delChannel(Map map);

    void updatePolling(Map map);

    void closeChannel(Long channelId);

    List<MerchantEntity> queryMerchantList(List<String> idList);

    List<MerchantEntity> queryMerchantListNormal(List<String> idList);

    void updatePriChannel(MerchantEntity merchantEntity);

    void closeTradeFlag(Long merchantrId);
}

