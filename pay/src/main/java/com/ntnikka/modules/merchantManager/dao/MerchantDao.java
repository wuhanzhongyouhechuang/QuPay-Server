package com.ntnikka.modules.merchantManager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ntnikka.modules.merchantManager.entity.MerchantEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @ClassName MerchantDao
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/18 16:32
 **/
@Repository
public interface MerchantDao extends BaseMapper<MerchantEntity> {
    void updateTradeStatus(Map<String, Object> params);

    MerchantEntity findByPriKey(String priKey);

    MerchantEntity findById(Long id);

    void updateSettleStatus(Map<String, Object> params);
}
