package com.ntnikka.modules.merchantManager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.modules.merchantManager.dao.ChannelDao;
import com.ntnikka.modules.merchantManager.entity.ChannelEntity;
import com.ntnikka.modules.merchantManager.service.ChannelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ChannelServiceImpl
 * @Author Liuq
 * @Description todo
 * @Date 2018/12/3 15:28
 **/
@Service("ChannelService")
public class ChannelServiceImpl extends ServiceImpl<ChannelDao , ChannelEntity> implements ChannelService {
    @Override
    public void save(List<ChannelEntity> channelEntityList) {
        this.insertBatch(channelEntityList);
    }

    @Override
    public List<ChannelEntity> queryChannelByMerchantid(Long merchantId) {
        return this.selectList( new EntityWrapper<ChannelEntity>().eq("merchant_id" , merchantId).eq("del_flag",0));
    }

    @Override
    public void batchSaveAndUpdate(List<ChannelEntity> channelEntityList) {
        this.insertOrUpdateBatch(channelEntityList);
    }
}
