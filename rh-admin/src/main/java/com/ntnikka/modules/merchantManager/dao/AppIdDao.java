package com.ntnikka.modules.merchantManager.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ntnikka.modules.merchantManager.entity.AppIdEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Liuq
 * @email 530775870@qq.com
 * @date 2018-10-10 11:56:51
 */
@Repository
public interface AppIdDao extends BaseMapper<AppIdEntity> {
    List<Long> queryAppid();

    AppIdEntity queryInfoByAppid(Map map);
}
