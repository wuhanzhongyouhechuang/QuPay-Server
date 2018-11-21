package com.ntnikka.modules.apply.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ntnikka.modules.apply.entity.MyGbEntity;
import org.springframework.stereotype.Repository;

/**
 * @author luk
 * @create 2018-04-10 14:22
 * @desc MyGbDao
 **/
@Repository
public interface MyGbDao extends BaseMapper<MyGbEntity> {

    Long getIdByCode(String code);

}
