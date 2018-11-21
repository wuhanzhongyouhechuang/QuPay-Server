package com.ntnikka.modules.pay.aliPay.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.modules.pay.aliPay.dao.AliNotifyDao;
import com.ntnikka.modules.pay.aliPay.entity.AliNotifyEntity;
import com.ntnikka.modules.pay.aliPay.service.AliNotifyService;
import org.springframework.stereotype.Service;

/**
 * @ClassName AliNotifyServiceImpl
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/25 15:41
 **/
@Service("AliNotifyService")
public class AliNotifyServiceImpl extends ServiceImpl<AliNotifyDao, AliNotifyEntity> implements AliNotifyService {
    @Override
    public void save(AliNotifyEntity aliNotifyEntity) {
        this.insert(aliNotifyEntity);
    }
}
