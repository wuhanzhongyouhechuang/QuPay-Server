package com.ntnikka.modules.pay.aliPay.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.pay.aliPay.entity.AliNotifyEntity;

/**
 * @ClassName AliNotifyService
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/25 15:39
 **/
public interface AliNotifyService extends IService<AliNotifyEntity> {

    void save(AliNotifyEntity aliNotifyEntity);

}
