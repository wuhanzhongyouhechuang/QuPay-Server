
package com.ntnikka.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.sys.entity.SysDictEntity;
import com.ntnikka.utils.PageUtils;

import java.util.Map;

/**
 * 数据字典
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

