package com.ntnikka.modules.merchantManager.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.merchantManager.entity.ChannelEntity;

import java.util.List;

/**
 * @ClassName ChannelService
 * @Author Liuq
 * @Description todo
 * @Date 2018/12/3 15:27
 **/
public interface ChannelService extends IService<ChannelEntity> {
    void save(List<ChannelEntity> channelEntityList);

    List<ChannelEntity> queryChannelByMerchantid(Long merchantId);

    void batchSaveAndUpdate(List<ChannelEntity> channelEntityList);
}