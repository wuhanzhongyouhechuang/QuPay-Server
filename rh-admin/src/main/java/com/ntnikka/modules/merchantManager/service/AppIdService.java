package com.ntnikka.modules.merchantManager.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.common.utils.PageUtils;
import com.ntnikka.modules.merchantManager.entity.AppIdEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Liuq
 * @email 530775870@qq.com
 * @date 2018-10-10 11:56:51
 */
public interface AppIdService extends IService<AppIdEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<Long> queryAppid();

    AppIdEntity queryInfoByAppid(Map map);
}

