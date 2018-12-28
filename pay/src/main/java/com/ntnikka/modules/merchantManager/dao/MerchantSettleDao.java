package com.ntnikka.modules.merchantManager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ntnikka.modules.merchantManager.entity.MerchantSettleChannel;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @ClassName MerchantSettleDao
 * @Author Liuq
 * @Description todo
 * @Date 2018/12/25 11:05
 **/
@Repository
public interface MerchantSettleDao extends BaseMapper<MerchantSettleChannel> {
    void updateSettleChannel(Map map);

    void closeSettleChannel(Integer settleChannelId);
}
